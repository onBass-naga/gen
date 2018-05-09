package com.areab.gen

import groovy.transform.CompileStatic


@CompileStatic
class Command {
    static Command init(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("args length ZERO")
        }
    }
}