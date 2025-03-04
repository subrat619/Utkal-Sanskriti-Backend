package com.cyfrifpro.service;

import com.cyfrifpro.DTO.TempleAmountsDTO;

public interface TempleAmountsService {
	TempleAmountsDTO createTempleAmounts(TempleAmountsDTO dto);

	TempleAmountsDTO updateTempleAmounts(Long templeId, TempleAmountsDTO dto);

	TempleAmountsDTO getTempleAmountsByTempleId(Long templeId);

	void deleteTempleAmounts(Long templeId);
}
