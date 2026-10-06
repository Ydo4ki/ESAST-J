package com.ydo4ki.esast;

import java.io.*;

final class $esast$1$0$0 {
    private $esast$1$0$0() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    static String readAllLinesJoined(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) {
                    sb.append('\n');
                }
                sb.append(line);
                first = false;
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) { }
            }
        }
    }
}
