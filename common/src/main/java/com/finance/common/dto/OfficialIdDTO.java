package com.finance.common.dto;


import com.finance.common.enums.OfficialIdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficialIdDTO extends BaseDTO {
	private OfficialIdTypeEnum type;
	private String value;
	private Date expiryDate;
}
