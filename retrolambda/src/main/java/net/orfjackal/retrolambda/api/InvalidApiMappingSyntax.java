// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.api;

import java.io.IOException;

public class InvalidApiMappingSyntax extends IOException {

    public InvalidApiMappingSyntax(String message) {
        super(message);
    }


    public InvalidApiMappingSyntax(int lineNumber, String message) {
        super("Line " + (lineNumber + 1) + ": " + message);
    }
}