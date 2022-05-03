package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.Statement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class StatementInDto extends Statement {

    private MultipartFile postFile;

    public StatementInDto(StatementInDto statementInDto) throws IOException {
        super(statementInDto);
        this.postFile = statementInDto.getPostFile();
    }
}
