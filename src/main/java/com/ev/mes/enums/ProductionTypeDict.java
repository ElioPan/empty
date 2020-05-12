package com.ev.mes.enums;

public enum ProductionTypeDict {
	ORDINARY_ORDER(306L, "普通订单"), PROCESS_TRACKING(235L, "工序跟踪"), PROJECT_MANUFACTURING(289L, "项目制造");

	private final long id;
	private final String name;

	ProductionTypeDict(long id, String name) {
		this.name = name;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
