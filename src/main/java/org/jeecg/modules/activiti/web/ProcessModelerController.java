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
package org.jeecg.modules.activiti.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Model;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.activiti.entity.ProcessModelDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("activiti/activitiService")
public class ProcessModelerController {

  @Autowired private RepositoryService repositoryService;

  @RequestMapping(value = "/model/save", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  public void saveMode(@RequestBody ProcessModelDTO processModelDTO) throws IOException {
    Model model = repositoryService.getModel(processModelDTO.getModelEntity().getId());
    if (Objects.isNull(model)) {
      model = repositoryService.newModel();
    }
    model.setMetaInfo(processModelDTO.getModelEntity().getMetaInfo());
    model.setName(processModelDTO.getModelEntity().getName());
    model.setKey(processModelDTO.getModelEntity().getKey());

    repositoryService.saveModel(model);
    repositoryService.addModelEditorSource(
        model.getId(), processModelDTO.getBpm().getBytes(StandardCharsets.UTF_8));
    repositoryService.addModelEditorSourceExtra(
        model.getId(), processModelDTO.getSvg().getBytes(StandardCharsets.UTF_8));
    return;
  }

  @RequestMapping(value = "/model/{modelId}", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  public Result<ProcessModelDTO> getModel(@PathVariable String modelId) throws IOException {
    Model model = repositoryService.getModel(modelId);

    ProcessModelDTO processModelDTO = new ProcessModelDTO(new ModelEntity());
    BeanUtils.copyProperties(model, processModelDTO.getModelEntity());
    processModelDTO.setBpm(
        new String(repositoryService.getModelEditorSource(modelId), StandardCharsets.UTF_8));
    return Result.ok(processModelDTO);
  }
}
