package com.leo.ai.ollamachat.ingestion;

import java.util.List;

public interface TextChunker {

    List<String> chunk(String text);

}
