package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiWorker;
import com.dev.nicehash.domain.models.WorkerHub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex Gladkov on 06.08.18.
 * Implementation for worker converter
 */
public class WorkerConverterImpl implements WorkerConverter {

    @Override
    public WorkerHub.Worker apiToModel(ApiWorker apiWorker) {
        return new WorkerHub.Worker(apiWorker.getTitle(), 0.0f, apiWorker.getTime(), 0.0f, 0.0f);
    }

    @Override
    public List<WorkerHub> workerToHub(List<WorkerHub.Worker> workers) {
        List<WorkerHub> workerHubs = new LinkedList<>();
        for (WorkerHub.Worker worker : workers) {
            WorkerHub existingHub = null;
            for (WorkerHub hub : workerHubs) {
                if (hub.getTitle().equals(worker.getTitle())) {
                    existingHub = hub;
                    break;
                }
            }

            if (existingHub == null) {
                List<WorkerHub.Worker> titles = new ArrayList<>();
                titles.add(worker);
                workerHubs.add(new WorkerHub(worker.getTitle(), "", titles));
            } else {
                // Kotlin bug ported as-is: added to a temporary mutable copy, result discarded
                new ArrayList<>(existingHub.getWorkerTitles()).add(worker);
            }
        }

        return workerHubs;
    }
}
