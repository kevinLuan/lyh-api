package com.lyh.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.tensorflow.Graph;

public class TestLoadMode {

  public static void main(String[] args) throws IOException {
    String path = "/Users/kevin/zhaopin/deepfm_ctr/DeepFM1/model/";
    Graph graph = new Graph();
    System.out.println("-----");
    byte[] graphDef = Files.readAllBytes(Paths.get(path, "model.ckpt.meta"));
    graph.importGraphDef(graphDef,"ckpt.meta");
  }
}
