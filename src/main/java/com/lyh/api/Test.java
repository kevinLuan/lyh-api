package com.lyh.api;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.tensorflow.*;
import java.util.List;

/**
 * https://blog.csdn.net/ling913/article/details/80186093
 */
public class Test {

  //构造测试数据，用的是mnist测试集的第15个， mnist.test.images[15]，label是数字5
  static float[][] a = new float[1][784];

  static {
    a[0] = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.2f, 0.517647f, 0.839216f, 0.992157f,
        0.996078f, 0.992157f, 0.796079f, 0.635294f, 0.160784f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0.4f, 0.556863f, 0.796079f, 0.796079f, 0.992157f, 0.988235f,
        0.992157f, 0.988235f, 0.592157f, 0.27451f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0.996078f, 0.992157f, 0.956863f, 0.796079f, 0.556863f, 0.4f,
        0.321569f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0.67451f, 0.988235f, 0.796079f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.0823529f, 0.87451f, 0.917647f, 0.117647f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0.478431f, 0.992157f, 0.196078f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.482353f, 0.996078f, 0.356863f, 0.2f, 0.2f,
        0.2f, 0.0392157f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0.0823529f, 0.87451f, 0.992157f, 0.988235f, 0.992157f, 0.988235f, 0.992157f,
        0.67451f, 0.321569f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0.0823529f, 0.839216f, 0.992157f, 0.796079f, 0.635294f, 0.4f, 0.4f, 0.796079f, 0.87451f,
        0.996078f, 0.992157f, 0.2f, 0.0392157f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0.239216f, 0.992157f, 0.670588f, 0f, 0f, 0f, 0f, 0f, 0.0784314f, 0.439216f,
        0.752941f, 0.992157f, 0.831373f, 0.160784f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.4f, 0.796079f, 0.917647f, 0.2f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0.0784314f, 0.835294f, 0.909804f, 0.321569f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.243137f, 0.796079f, 0.917647f,
        0.439216f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0.0784314f, 0.835294f, 0.988235f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.6f, 0.992157f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0.160784f, 0.913726f, 0.831373f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.443137f,
        0.360784f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.121569f, 0.678431f, 0.956863f,
        0.156863f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.321569f, 0.992157f, 0.592157f, 0f,
        0f, 0f, 0f, 0f, 0f, 0.0823529f, 0.4f, 0.4f, 0.717647f, 0.913726f, 0.831373f, 0.317647f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.321569f, 1.0f, 0.992157f, 0.917647f,
        0.596078f, 0.6f, 0.756863f, 0.678431f, 0.992157f, 0.996078f, 0.992157f, 0.996078f,
        0.835294f, 0.556863f, 0.0784314f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0.278431f, 0.592157f, 0.592157f, 0.909804f, 0.992157f, 0.831373f, 0.752941f, 0.592157f,
        0.513726f, 0.196078f, 0.196078f, 0.0392157f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.0f};
  }

  public static void test() throws IOException {
//    SavedModelBundle b = SavedModelBundle
//        .load("/Users/kevin/zhaopin/deepfm_ctr/DeepFM1/model2","mytag");
    SavedModelBundle b = SavedModelBundle
        .load("/Users/kevin/zhaopin/deepfm_ctr/DeepFM1/model2", "mytag");
    Session tfSession = b.session();
    Operation operationPredict = b.graph().operation("predict");   //要执行的op
    Output output = operationPredict.output(0);
    Tensor input_x = Tensor.create(a);
    List<Tensor<?>> out = tfSession.runner().feed("input_x", input_x).fetch(output).run();
    for (Tensor s : out) {
      float[][] t = new float[1][10];
      s.copyTo(t);
      for (float i : t[0]) {
        System.out.println(i);
      }
    }
  }

  public static void test2() throws IOException {
    byte[] graphDef = Files
        .readAllBytes(Paths
            .get("/Users/kevin/java_home/deepfm_ctr/DeepFM1/rpc_package/pb/", "frozen_model.pb"));
    Graph graph = new Graph();
    graph.importGraphDef(graphDef);
    Session tfSession = new Session(graph);
    long start = System.currentTimeMillis();
    Operation operationPredict = graph.operation("out");//要执行的op
    Output output = operationPredict.output(0);
    int[][] array = new int[2][380];
    array[0] = new int[380];
    array[1] = new int[380];
    Tensor input_x = Tensor.create(array);
    float[][] input_y1 = new float[2][380];
    input_y1[0] = new float[380];
    input_y1[1] = new float[380];
    Tensor input_y = Tensor.create(input_y1);
    List<Tensor<?>> out = tfSession.runner().feed("feat_index", input_x)
        .feed("feat_value", input_y)
        .feed("dropout_keep_fm", Tensor.create(new float[]{1.0f, 1.0f}))
        .feed("dropout_deep_deep", Tensor.create(new float[]{0.5f, 0.5f, 0.5f, 0.5f}))
        .fetch(output).run();
    System.out.println(new Gson().toJson(out));
    for (Tensor s : out) {
      float[][] t = new float[2][1];
      s.copyTo(t);
      for (int i = 0; i < t.length; i++) {
        for (int j = 0; j < t[i].length; j++) {
          System.out.println(t[i][j]);
        }
      }
    }
    System.out.println("耗时:" + (System.currentTimeMillis() - start));
  }

  public static void main(String[] args) throws IOException {
//    test();
    test2();
  }
}