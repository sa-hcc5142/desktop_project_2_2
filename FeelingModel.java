package com.example.login;
public class FeelingModel {
    private static FeelingModel instance;
    private double S1, S2, S3;

    private FeelingModel() {
        // Private constructor to prevent instantiation
    }

    public static FeelingModel getInstance() {
        if (instance == null) {
            instance = new FeelingModel();
        }
        return instance;
    }
    public void setS1(double S1) {
        this.S1 = S1;
    }

    public double getS1() {
        return S1;
    }

    public void setS2(double S2) {
        this.S2 = S2;
    }
    public double getS2() {
        return S2;
    }

    public void setS3(double S3) {
        this.S3 = S3;
    }
    public double getS3() {
        return S3;
    }


}
