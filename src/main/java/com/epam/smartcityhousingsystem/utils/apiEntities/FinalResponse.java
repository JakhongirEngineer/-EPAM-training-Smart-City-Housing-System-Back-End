package com.epam.smartcityhousingsystem.utils.apiEntities;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * FinalResponse class is used to represent response coming from City Administration module
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ApiModel(description = "Final Response coming from City Administration project")
public class FinalResponse {
    @ApiModelProperty(notes = "success or failure message", example = "OK")
    private Boolean success;
    @ApiModelProperty(notes = "Object that illustrates ownership")
    private List<Ownership> result;

    public FinalResponse() {
    }

    public FinalResponse(Boolean success, List<Ownership> result) {
        this.success = success;
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Ownership> getResult() {
        return result;
    }

    public void setResult(List<Ownership> result) {
        this.result = result;
    }
}
