// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.api;

class Mapping {

    private final String owner;
    private final String name;
    private final String desc;

    Mapping(String signature) {
        String[] parts = signature.split("\\.");
        this.owner = parts[0];

        int descStart = parts[1].indexOf('(');
        if(descStart == -1) {
            this.name = parts[1];
            this.desc = null;
        } else {
            this.name = parts[1].substring(0, descStart);
            this.desc = parts[1].substring(descStart);
        }
    }

    public Mapping(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.desc = null;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
