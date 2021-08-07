package com.fgrapp.file.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.file.domain.FileResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * FileMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:26
 */
@Mapper
@Component
public interface FileMapper extends FgrMapper<FileResponse> {
}
