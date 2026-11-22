package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiWorker;
import com.dev.nicehash.domain.models.WorkerHub;

import java.util.List;

/**
 * Created by Alex Gladkov on 06.08.18.
 * Converter between api, app and db layer for workers
 */
public interface WorkerConverter {
    WorkerHub.Worker apiToModel(ApiWorker apiWorker);
    List<WorkerHub> workerToHub(List<WorkerHub.Worker> workers);
}
