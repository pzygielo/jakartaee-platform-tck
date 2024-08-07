/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.core.derivedid.ex3b;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

/**
 *
 * @author Raja Perumal
 */

@Entity
public class DID3bEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	DID3bEmployeeId empId;

	public DID3bEmployee() {
	}

	public DID3bEmployee(DID3bEmployeeId eId) {
		this.empId = eId;
	}

	public DID3bEmployeeId getEmpId() {
		return empId;
	}

	public void setEmpId(DID3bEmployeeId empId) {
		this.empId = empId;
	}
}
