package com.system.bibliotec.model.enums;

public enum StatusCliente {

	INADIMPLENTE("Inadiplente"), ADIMPLENTE("Adimplente");

	private String statusCliente;

	StatusCliente(String statusCliente) {

		this.statusCliente = statusCliente;
	}

	public String getstatusCliente() {
		return statusCliente;
	}

	public void setstatusCliente(String statusCliente) {
		this.statusCliente = statusCliente;
	}

	/*
	 * @JsonCreator public static StatusCliente fromValueString(String
	 * statusCliente) { if (statusCliente == null) { throw new
	 * IllegalArgumentException(); } for (StatusCliente statusClienteSalved :
	 * values()) { if (statusCliente.equals(statusClienteSalved.getstatusCliente()))
	 * { return statusClienteSalved; } } throw new IllegalArgumentException(); }
	 */

}
