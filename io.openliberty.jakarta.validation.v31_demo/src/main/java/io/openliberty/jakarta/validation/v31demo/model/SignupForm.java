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

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * SignupForm record demonstrating group sequence validation with a Java record.
 */
public record SignupForm(
                @NotBlank(message = "Name cannot be blank", groups = FirstGroup.class) String firstName,
                @Min(value = 18, message = "Age must be at least 18", groups = SecondGroup.class) int age) {
    // No additional methods needed
}
