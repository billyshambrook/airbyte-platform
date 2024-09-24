package io.airbyte.connector.rollout.client

import io.airbyte.commons.temporal.factories.WorkflowClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.temporal.client.WorkflowClient
import io.temporal.common.converter.DataConverter
import jakarta.inject.Singleton

class WorkflowClientWrapper(
  private val workflowClient: WorkflowClient,
) {
  fun getClient(): WorkflowClient {
    return workflowClient
  }
}

@Factory
class ConnectorRolloutWorkflowClient {
  @Singleton
  fun workflowClient(
    temporalWorkflowServiceFactory: ConnectorRolloutTemporalWorkflowServiceFactory,
    temporalSdkTimeouts: TemporalSdkTimeouts,
    @Value("\${temporal.cloud.connectorRollout.namespace}") namespace: String?,
    dataConverter: DataConverter,
  ): WorkflowClientWrapper {
    val temporalWorkflowService = temporalWorkflowServiceFactory.createTemporalWorkflowServiceLazily(temporalSdkTimeouts)
    return WorkflowClientWrapper(WorkflowClientFactory().createWorkflowClient(temporalWorkflowService, namespace!!, dataConverter))
  }
}
