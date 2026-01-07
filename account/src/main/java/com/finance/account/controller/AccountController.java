package com.finance.account.controller;

import com.finance.account.service.AccountService;
import com.finance.common.controller.BaseController;
import com.finance.common.dto.AccountDTO;
import com.finance.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AccountController extends BaseController<AccountDTO> {

	@Autowired
	private AccountService accountService;

	@Override
	protected BaseService<AccountDTO> getService() {
		return accountService;
	}
}
