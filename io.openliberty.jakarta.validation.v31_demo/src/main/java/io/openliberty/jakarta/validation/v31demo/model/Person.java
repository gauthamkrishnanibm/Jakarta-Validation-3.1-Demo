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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Person record demonstrating basic validation constraints on a Java record.
 */
public record Person(@NotNull String name) {
    /**
     * Method to check name size with validation constraint
     * @param x The string to check
     * @return The string with additional text
     */
    public String checkNameSize(@Size(max = 10) String x) {
        return x + "String value";
    }

    /**
     * Override the accessor method to add validation constraint
     * @return The name field
     */
    @Size(min = 6)
    public String getName() {
        return this.name;
    }
}
