/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowable.cmmn.rest.service.api.history.task;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.rest.service.api.CmmnRestResponseFactory;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * @author Tijs Rademakers
 */
@RestController
@Api(tags = { "History Task" }, description = "Manage History Task Instances", authorizations = { @Authorization(value = "basicAuth") })
public class HistoricTaskInstanceIdentityLinkCollectionResource extends HistoricTaskInstanceBaseResource {

    @Autowired
    protected CmmnRestResponseFactory restResponseFactory;

    @Autowired
    protected CmmnHistoryService historyService;

    @ApiOperation(value = "List identity links of a historic task instance", nickname ="listHistoricTaskInstanceIdentityLinks", tags = { "History Task" }, notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates request was successful and the identity links are returned", response = HistoricIdentityLinkResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Indicates the task instance could not be found.") })
    @GetMapping(value = "/cmmn-history/historic-task-instances/{taskId}/identitylinks", produces = "application/json")
    public List<HistoricIdentityLinkResponse> getTaskIdentityLinks(@ApiParam(name = "taskId") @PathVariable String taskId, HttpServletRequest request) {
        HistoricTaskInstance task = getHistoricTaskInstanceFromRequestWithoutAccessCheck(taskId);

        if (restApiInterceptor != null) {
            restApiInterceptor.accessHistoricTaskIdentityLinks(task);
        }
        
        List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(task.getId());

        if (identityLinks != null) {
            return restResponseFactory.createHistoricIdentityLinkResponseList(identityLinks);
        }

        return new ArrayList<>();
    }
}
