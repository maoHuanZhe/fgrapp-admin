package com.fgrapp.base.result.exception;

import com.fgrapp.base.result.ResultStatus;
import lombok.Getter;

/**
 * ResultException
 *
 * @author fan guang rui
 * @date 2021年06月09日 14:11
 */
@Getter
public class ResultException extends RuntimeException {

    /**
     * 业务异常信息信息
     */
    ResultStatus resultStatus;

    public ResultException() {
        this(ResultStatus.INTERNAL_SERVER_ERROR);
    }

    public ResultException(ResultStatus resultStatus) {
        super(resultStatus.getMessage());
        this.resultStatus = resultStatus;
    }

    public ResultException(String msg){
        super(msg);
        this.resultStatus = ResultStatus.INTERNAL_SERVER_ERROR;
    }
}
