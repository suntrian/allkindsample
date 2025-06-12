package com.example.airbyte

import com.airbyte.api.Airbyte
import com.airbyte.api.models.errors.SDKError
import com.airbyte.api.models.operations.DeleteSourceRequest
import com.airbyte.api.models.operations.DeleteWorkspaceRequest
import com.airbyte.api.models.operations.GetSourceRequest
import com.airbyte.api.models.operations.ListConnectionsRequest
import com.airbyte.api.models.operations.ListDestinationsRequest
import com.airbyte.api.models.operations.ListJobsRequest
import com.airbyte.api.models.operations.ListPermissionsRequest
import com.airbyte.api.models.operations.ListSourceDefinitionsRequest
import com.airbyte.api.models.operations.ListSourcesRequest
import com.airbyte.api.models.operations.ListUsersWithinAnOrganizationRequest
import com.airbyte.api.models.operations.ListWorkspacesRequest
import com.airbyte.api.models.operations.PutSourceRequest
import com.airbyte.api.models.shared.ConnectionCreateRequest
import com.airbyte.api.models.shared.ConnectionStatusEnum
import com.airbyte.api.models.shared.ConnectionSyncModeEnum
import com.airbyte.api.models.shared.DestinationConfiguration
import com.airbyte.api.models.shared.DestinationCreateRequest
import com.airbyte.api.models.shared.DestinationPostgres
import com.airbyte.api.models.shared.EmailNotificationConfig
import com.airbyte.api.models.shared.JobCreateRequest
import com.airbyte.api.models.shared.JobType
import com.airbyte.api.models.shared.JobTypeEnum
import com.airbyte.api.models.shared.JobTypeResourceLimit
import com.airbyte.api.models.shared.NamespaceDefinitionEnum
import com.airbyte.api.models.shared.NotificationConfig
import com.airbyte.api.models.shared.NotificationsConfig
import com.airbyte.api.models.shared.PrivateToken
import com.airbyte.api.models.shared.ResourceRequirements
import com.airbyte.api.models.shared.SchemeClientCredentials
import com.airbyte.api.models.shared.ScopedResourceRequirements
import com.airbyte.api.models.shared.Security
import com.airbyte.api.models.shared.SourceConfiguration
import com.airbyte.api.models.shared.SourceCreateRequest
import com.airbyte.api.models.shared.SourceGitlab
import com.airbyte.api.models.shared.SourceGitlabAuthorizationMethod
import com.airbyte.api.models.shared.SourcePutRequest
import com.airbyte.api.models.shared.StreamConfiguration
import com.airbyte.api.models.shared.StreamConfigurationsInput
import com.airbyte.api.models.shared.WebhookNotificationConfig
import com.airbyte.api.models.shared.WorkspaceCreateRequest
import com.airbyte.api.models.shared.WorkspaceResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.junit.jupiter.api.function.ThrowingSupplier
import org.springframework.http.HttpStatus
import java.nio.charset.StandardCharsets
import kotlin.jvm.optionals.getOrNull

@ExtendWith(AirbyteTest.TestExceptionExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AirbyteTest {

    private lateinit var sdk: Airbyte

    private val organizationId = "00000000-0000-0000-0000-000000000000"


    @BeforeAll
    fun setUp() {
//        sdk = Airbyte.builder()
//            .serverURL("http://localhost:8000/api/public/v1")
//            .security(Security.builder()
//                .basicAuth(
//                    SchemeBasicAuth.builder()
//                    .username("test@abc.com")
//                    .password("123456")
//                    .build())
//                .build()
//            )
//            .build()
        sdk = Airbyte.builder()
            .serverURL("http://localhost:8000/api/public/v1")
            .security(Security.builder()
                .clientCredentials(SchemeClientCredentials.builder()
                    .clientID("da1615e6-136e-4181-8c19-5c499f1d4ec3")
                    .clientSecret("slDr6HtpUHnXORPcdKH8LyY7yATHE9DO")
                    .tokenURL("/api/public/v1/applications/token")
                    .build())
                .build())
                .build()
    }


    @Test
    fun testListWorkSpace() {
        val request = ListWorkspacesRequest.builder().build()
        val response = sdk.workspaces().listWorkspaces().request(request).call()
        val workspaces = response.workspacesResponse().getOrNull()
        Assertions.assertNotNull(workspaces)
        for (ws in workspaces!!.data()) {
            println("${ws.name()}:${ws.workspaceId()}")
        }

        val createReq = WorkspaceCreateRequest.builder()
            .name("test workspace(${workspaces?.data()?.size?:1})")
            .organizationId(organizationId)
            .notifications(NotificationsConfig.builder()
                .failure(NotificationConfig.builder().email(EmailNotificationConfig.builder().enabled(true).build()).build())
                .connectionUpdate(NotificationConfig.builder().webhook(WebhookNotificationConfig.builder().url("http://localhost:8000").enabled(true).build()).build())
                .build()
            )
            .build()
        val createResp = Assertions.assertDoesNotThrow(ThrowingSupplier{
            sdk.workspaces().createWorkspace(createReq)
        })
        val created = createResp.workspaceResponse().getOrNull()
        Assertions.assertNotNull(created)
        Assertions.assertEquals(createReq.name(), created!!.name())

        val deleteRequest = DeleteWorkspaceRequest.builder()
            .workspaceId(created.workspaceId())
            .build()
        val deleteResponse = Assertions.assertDoesNotThrow (ThrowingSupplier {
            sdk.workspaces().deleteWorkspace().request(deleteRequest).call()
        })
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode())
    }

    @Test
    fun testPermissions() {
        val request = ListPermissionsRequest.builder().organizationId(organizationId).build()
        val response = sdk.permissions().listPermissions().request(request).call()
        val permissions = response.permissionsResponse().getOrNull()?.data() ?: emptyList()
        for (permit in permissions) {
            println("${permit.permissionId()}: ${permit.permissionType()}: ${permit.scope()}: ${permit.scopeId()}")
        }
    }

    @Test
    fun testOrganizations() {
        val response = sdk.organizations().listOrganizationsForUser().call()
        val organizations = response.organizationsResponse().getOrNull()?.data() ?: emptyList()
        for (org in organizations) {
            println("${org.organizationId()}: ${org.organizationName()}: ${org.email()}")
        }
    }

    @Test
    fun testUsers() {
        val reqeust = ListUsersWithinAnOrganizationRequest.builder()
            .organizationId(organizationId)
            .build()
        val response = sdk.users().listUsersWithinAnOrganization(reqeust)
        val users = response.usersResponse().getOrNull()?.data() ?: emptyList()
        for (user in users) {
            println("${user.name()}@${user.email()}#${user.id()}")
        }
    }

    @Test
    fun testSourceDefinitions() {
        val workspace = getWorkspace("new workspace")
        val request = ListSourceDefinitionsRequest.builder()
            .workspaceId(workspace.workspaceId())
            .build()
        val response = sdk.sourceDefinitions().listSourceDefinitions(request)
        val definitions = response.definitionsResponse().getOrNull()?.data()?:emptyList()
        for (def in definitions) {
            println(def)
        }
    }


    @Test
    fun testConnections() {
        val workspace = getWorkspace("new workspace")
        val workspaceId = workspace.workspaceId()
        println("workspaceId: $workspaceId")

        val sourceListResponse = sdk.sources().listSources(ListSourcesRequest.builder()
            .workspaceIds(listOf(workspaceId)).build())
        val sourceList = sourceListResponse.sourcesResponse().getOrNull()?.data() ?: emptyList()
        for (source in sourceList) {
            println(source)
        }

        val sourceCreateRequest = SourceCreateRequest.builder()
            .workspaceId(workspaceId)
            .name("gitlab")
            .definitionId("5e6175e5-68e1-4c17-bff9-56103bbb0d80")
            .configuration(SourceConfiguration.of(SourceGitlab.builder()
                .apiUrl("https://git.ringcentral.com/")
                .credentials(SourceGitlabAuthorizationMethod.of(PrivateToken.builder()
                    .accessToken("HN78qJwHMJ_eWfmNbamL")
                    .build()))
                .build()))
            .build()
        val sourceId = if (!sourceList.any { it.name() == sourceCreateRequest.name() }) {
            val sourceCreateResponse = sdk.sources().createSource().request(sourceCreateRequest).call()
            val sourceResponse = sourceCreateResponse.sourceResponse().getOrNull()
            Assertions.assertNotNull(sourceResponse)
            println(sourceResponse)
            sourceResponse!!.sourceId()
        } else {
            sourceList.first { it.name() == sourceCreateRequest.name() }.sourceId()
        }

        val destinationListResponse = sdk.destinations().listDestinations(ListDestinationsRequest.builder()
            .workspaceIds(listOf(workspaceId)).build())
        val destinationList = destinationListResponse.destinationsResponse().getOrNull()?.data()?: emptyList()
        for (destination in destinationList) {
            println(destination)
        }

        val destinationCreateRequest = DestinationCreateRequest.builder()
            .workspaceId(workspaceId)
            .name("postgresql")
            .definitionId("")
            .resourceAllocation(ScopedResourceRequirements.builder()
                .jobSpecific(listOf(JobTypeResourceLimit.builder()
                    .jobType(JobType.SYNC)
                    .resourceRequirements(ResourceRequirements.builder()
                        .cpuLimit("1C")
                        .build())
                    .build()))
                .build())
            .configuration(DestinationConfiguration.of(DestinationPostgres.builder()
                .username("postgres")
                .password("abc13")
                .database("test")
                .host("localhost")
                .port(5432)
                .build()))
            .build()
        val destinationId = if (!destinationList.any{ it.name() == destinationCreateRequest.name() }) {
            val destinationCreateResponse = sdk.destinations().createDestination().request(destinationCreateRequest).call()
            val destinationResponse = destinationCreateResponse.destinationResponse().getOrNull()
            Assertions.assertNotNull(destinationResponse)
            println(destinationResponse)
            destinationResponse!!.destinationId()
        } else {
            destinationList.first { it.name() == destinationCreateRequest.name() }.destinationId()
        }

        val listConnections = sdk.connections()
            .listConnections(ListConnectionsRequest.builder().workspaceIds(listOf(workspaceId)).build())
            .connectionsResponse().getOrNull()?.data()?: emptyList()
        var connection = listConnections.firstOrNull { it.sourceId() == sourceId && it.destinationId() == destinationId }
        if (connection == null) {
            val createConnectionResponse = sdk.connections().createConnection(
                ConnectionCreateRequest.builder()
                    .name("gitlab to postgresql")
                    .status(ConnectionStatusEnum.INACTIVE)
                    .prefix("prefixed_")
                    .sourceId(sourceId)
                    .destinationId(destinationId)
                    .namespaceDefinition(NamespaceDefinitionEnum.DESTINATION)
                    .namespaceFormat("test")
                    .configurations(StreamConfigurationsInput.builder()
                        .streams(listOf(StreamConfiguration.builder()
                            .name("stream_name")
                            .syncMode(ConnectionSyncModeEnum.INCREMENTAL_APPEND)
                            .build()))
                        .build())
                    .build()
            )
            val response = createConnectionResponse.connectionResponse().getOrNull()
            Assertions.assertNotNull(response)
            println(response)
            connection = response
        }

        val listJobsResponse = sdk.jobs().listJobs(
            ListJobsRequest.builder()
                .workspaceIds(listOf(workspaceId))
                .build())
        val jobs = listJobsResponse.jobsResponse().getOrNull()?.data() ?: emptyList()
        for (job in jobs) {
            println(job)
        }
        if (jobs.isEmpty()) {
            val createJobResponse = sdk.jobs().createJob(
                JobCreateRequest.builder()
                    .jobType(JobTypeEnum.SYNC)
                    .connectionId(connection!!.connectionId())
                    .build())
            val job = createJobResponse.jobResponse().getOrNull()
            println(job)
        }

    }


    private fun getWorkspace(name: String? = null): WorkspaceResponse {
        val request = ListWorkspacesRequest.builder()
            .limit(5)
            .offset(0)
            .build()
        val response = sdk.workspaces().listWorkspaces().request(request).call()
        return response.workspacesResponse().getOrNull()?.data()
            ?.filter { name.isNullOrBlank() || it.name().contains(name) }
            ?.lastOrNull()
            ?: throw IllegalStateException("Not Found Workspace")
    }

    class TestExceptionExtension(): TestExecutionExceptionHandler {
        override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
            if (throwable is SDKError) {
                val body = throwable.body().toString(StandardCharsets.UTF_8)
                throwable.printStackTrace()
                print(body)
            }
        }
    }
}