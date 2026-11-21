#!/usr/bin/env python3
"""Аналитика по собранным вакансиям: ИИ vs бейзлайн, вилки, навыки, грейды."""
import json
import re
import statistics
from collections import Counter
from pathlib import Path

SRC = Path(__file__).parent / "vacancies.jsonl"

AI_RX = re.compile(
    r"\b(llm|ml|ai|nlp|rag|gpt|genai|ии|дс|data scien|machine learning|deep learning|"
    r"нейросет|нейронн|искусственн\w+ интеллект|промпт|prompt|langchain|langgraph|"
    r"агент\w* ии|ai.?agent|copilot|claude|мл|ml.?engineer|computer vision|cv.?engineer|"
    r"fine.?tun|мо\b|машинн\w+ обучен)\b",
    re.IGNORECASE,
)

def is_ai(v: dict) -> bool:
    hay = (v.get("title") or "") + " " + " ".join(v.get("skills") or [])
    return bool(AI_RX.search(hay))

def midpoint(v: dict):
    s = v.get("salary") or {}
    cur = s.get("currency")
    if cur and cur not in ("rur", "RUR", "rub", "RUB", "₽"):
        return None
    lo, hi = s.get("from"), s.get("to")
    if lo and hi:
        return (lo + hi) / 2
    return lo or hi or None

def fmt(x):
    return f"{round(x / 1000)}k" if x else "—"

def med(vals):
    return statistics.median(vals) if vals else None

def main() -> None:
    vacs = [json.loads(l) for l in SRC.open(encoding="utf-8")]
    ai = [v for v in vacs if is_ai(v)]
    base = [v for v in vacs if not is_ai(v) and "baseline" in v["tags"]]

    print(f"Всего собрано: {len(vacs)} | ИИ-вакансий (по title+skills): {len(ai)} | бейзлайн без ИИ: {len(base)}")

    ai_sal = [midpoint(v) for v in ai if midpoint(v)]
    base_sal = [midpoint(v) for v in base if midpoint(v)]
    print(f"\nВилка указана: ИИ {len(ai_sal)}/{len(ai)} ({100*len(ai_sal)//max(len(ai),1)}%), "
          f"бейзлайн {len(base_sal)}/{len(base)} ({100*len(base_sal)//max(len(base),1)}%)")
    print(f"Медиана вилки: ИИ {fmt(med(ai_sal))} ₽ | бейзлайн {fmt(med(base_sal))} ₽")

    print("\nМедиана по грейдам (ИИ vs бейзлайн, только с вилкой):")
    for grade in ["Младший (Junior)", "Средний (Middle)", "Старший (Senior)", "Ведущий (Lead)"]:
        a = [midpoint(v) for v in ai if v.get("grade") == grade and midpoint(v)]
        b = [midpoint(v) for v in base if v.get("grade") == grade and midpoint(v)]
        print(f"  {grade:20s} ИИ: {fmt(med(a))} (n={len(a)})  | бейзлайн: {fmt(med(b))} (n={len(b)})")

    print("\nТоп-25 навыков в ИИ-вакансиях:")
    cnt = Counter(s for v in ai for s in v.get("skills") or [])
    for s, c in cnt.most_common(25):
        sal = [midpoint(v) for v in ai if s in (v.get("skills") or []) and midpoint(v)]
        print(f"  {s:30s} {c:3d} упом. | медиана вилки: {fmt(med(sal))} (n={len(sal)})")

    print("\nИИ-инструменты нового стека (упоминания в навыках+названии):")
    for kw in ["LLM", "RAG", "LangChain", "LangGraph", "MCP", "Cursor", "Copilot",
               "Claude", "ChatGPT", "промпт", "prompt", "агент", "agent", "vLLM",
               "Hugging", "PyTorch", "fine-tun"]:
        hits = [v for v in vacs if kw.lower() in ((v.get("title") or "") + " " + " ".join(v.get("skills") or [])).lower()]
        sal = [midpoint(v) for v in hits if midpoint(v)]
        if hits:
            print(f"  {kw:12s} {len(hits):3d} вак. | медиана вилки: {fmt(med(sal))} (n={len(sal)})")

    print("\nГрейды ИИ-вакансий:")
    for g, c in Counter(v.get("grade") or "не указан" for v in ai).most_common():
        print(f"  {g or 'не указан':22s} {c}")

    print("\nПримеры ИИ-вакансий с максимальными вилками:")
    top = sorted((v for v in ai if midpoint(v)), key=midpoint, reverse=True)[:8]
    for v in top:
        print(f"  {fmt(midpoint(v)):>6s} | {v['grade'] or '—':18s} | {v['title'][:60]} | {v['company']}")

if __name__ == "__main__":
    main()
