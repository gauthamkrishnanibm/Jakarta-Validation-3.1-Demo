/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.jakarta.validation.v31demo.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * Registration record demonstrating validation groups with a Java record.
 */
public record Registration(
                @NotNull String companyid,
                @AssertTrue(
                            message = "Company should be registered",
                            groups = RegistrationChecks.class) boolean isRegistered) {
    // No additional methods needed
}
