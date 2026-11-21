#!/usr/bin/env python3
"""Сбор ИИ-вакансий с Хабр Карьеры (публичный frontend-API) для исследования
"за какие ИИ-навыки платят в 2026". Только stdlib."""
import json
import time
import urllib.parse
import urllib.request
from pathlib import Path

OUT_DIR = Path(__file__).parent
UA = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)"}
BASE = "https://career.habr.com/api/frontend/vacancies"

AI_QUERIES = [
    "LLM", "нейросети", "искусственный интеллект", "ChatGPT", "Claude",
    "Copilot", "Cursor", "промпт", "prompt engineering", "RAG",
    "ИИ-агент", "AI agent", "GenAI", "генеративный", "machine learning",
    "MCP", "vibe coding", "LangChain", "fine-tuning", "ML engineer",
]
# бейзлайн для сравнения вилок с "обычными" вакансиями
BASELINE_QUERIES = ["Android разработчик", "backend разработчик", "frontend разработчик"]

MAX_PAGES = 10
DELAY = 0.6


def fetch(query: str, page: int) -> dict:
    url = f"{BASE}?{urllib.parse.urlencode({'q': query, 'type': 'all', 'page': page})}"
    req = urllib.request.Request(url, headers=UA)
    with urllib.request.urlopen(req, timeout=30) as r:
        return json.load(r)


def collect(queries: list[str], tag: str, seen: dict) -> None:
    for q in queries:
        page, total_pages = 1, 1
        got = 0
        while page <= min(total_pages, MAX_PAGES):
            try:
                data = fetch(q, page)
            except Exception as e:
                print(f"  [{q}] p{page} error: {e}")
                break
            total_pages = data.get("meta", {}).get("totalPages", 1)
            for v in data.get("list", []):
                vid = v["id"]
                if vid not in seen:
                    seen[vid] = {
                        "id": vid,
                        "title": v.get("title"),
                        "company": (v.get("company") or {}).get("title"),
                        "grade": (v.get("salaryQualification") or {}).get("title"),
                        "salary": v.get("salary"),
                        "predictedSalary": v.get("predictedSalary"),
                        "skills": [s.get("title") for s in (v.get("skills") or [])],
                        "remote": v.get("remoteWork"),
                        "published": (v.get("publishedDate") or {}).get("date"),
                        "queries": [],
                        "tags": [],
                    }
                if q not in seen[vid]["queries"]:
                    seen[vid]["queries"].append(q)
                if tag not in seen[vid]["tags"]:
                    seen[vid]["tags"].append(tag)
                got += 1
            page += 1
            time.sleep(DELAY)
        total = data.get("meta", {}).get("totalResults", "?") if "data" in dir() else "?"
        print(f"  [{tag}] «{q}»: всего в выдаче {total}, собрано {got}")


def main() -> None:
    seen: dict = {}
    print("ИИ-срезы:")
    collect(AI_QUERIES, "ai", seen)
    print("Бейзлайн:")
    collect(BASELINE_QUERIES, "baseline", seen)

    out = OUT_DIR / "vacancies.jsonl"
    with out.open("w", encoding="utf-8") as f:
        for v in seen.values():
            f.write(json.dumps(v, ensure_ascii=False) + "\n")
    print(f"\nУникальных вакансий: {len(seen)} → {out}")


if __name__ == "__main__":
    main()
