package com.example.record_trail;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

public class SeoulData implements Serializable {

    private List<? extends Records> records;

    private List<? extends Fields> fields;

    public List<? extends Records> getRecords() {
        return this.records;
    }

    public void setRecords(List<? extends Records> records) {
        this.records = records;
    }

    public List<? extends Fields> getFields() {
        return this.fields;
    }

    public void setFields(List<? extends Fields> fields) {
        this.fields = fields;
    }

    public static class Records implements Serializable {
        private String 시작지점소재지지번주소;

        private String 관리기관명;

        private String 총소요시간;

        private String 시작지점도로명주소;

        private String 경로정보;

        private String 제공기관코드;

        private String 길소개;

        private String 총길이;

        private String 종료지점명;

        private String 시작지점명;

        private String 제공기관명;

        private String 길명;

        private String 종료지점소재지지번주소;

        private String 종료지점소재지도로명주소;

        private String 데이터기준일자;

        private String 관리기관전화번호;

        public String get시작지점소재지지번주소() {
            return this.시작지점소재지지번주소;
        }

        public void set시작지점소재지지번주소(String 시작지점소재지지번주소) {
            this.시작지점소재지지번주소 = 시작지점소재지지번주소;
        }

        public String get관리기관명() {
            return this.관리기관명;
        }

        public void set관리기관명(String 관리기관명) {
            this.관리기관명 = 관리기관명;
        }

        public String get총소요시간() {
            return this.총소요시간;
        }

        public void set총소요시간(String 총소요시간) {
            this.총소요시간 = 총소요시간;
        }

        public String get시작지점도로명주소() {
            return this.시작지점도로명주소;
        }

        public void set시작지점도로명주소(String 시작지점도로명주소) {
            this.시작지점도로명주소 = 시작지점도로명주소;
        }

        public String get경로정보() {
            return this.경로정보;
        }

        public void set경로정보(String 경로정보) {
            this.경로정보 = 경로정보;
        }

        public String get제공기관코드() {
            return this.제공기관코드;
        }

        public void set제공기관코드(String 제공기관코드) {
            this.제공기관코드 = 제공기관코드;
        }

        public String get길소개() {
            return this.길소개;
        }

        public void set길소개(String 길소개) {
            this.길소개 = 길소개;
        }

        public String get총길이() {
            return this.총길이;
        }

        public void set총길이(String 총길이) {
            this.총길이 = 총길이;
        }

        public String get종료지점명() {
            return this.종료지점명;
        }

        public void set종료지점명(String 종료지점명) {
            this.종료지점명 = 종료지점명;
        }

        public String get시작지점명() {
            return this.시작지점명;
        }

        public void set시작지점명(String 시작지점명) {
            this.시작지점명 = 시작지점명;
        }

        public String get제공기관명() {
            return this.제공기관명;
        }

        public void set제공기관명(String 제공기관명) {
            this.제공기관명 = 제공기관명;
        }

        public String get길명() {
            return this.길명;
        }

        public void set길명(String 길명) {
            this.길명 = 길명;
        }

        public String get종료지점소재지지번주소() {
            return this.종료지점소재지지번주소;
        }

        public void set종료지점소재지지번주소(String 종료지점소재지지번주소) {
            this.종료지점소재지지번주소 = 종료지점소재지지번주소;
        }

        public String get종료지점소재지도로명주소() {
            return this.종료지점소재지도로명주소;
        }

        public void set종료지점소재지도로명주소(String 종료지점소재지도로명주소) {
            this.종료지점소재지도로명주소 = 종료지점소재지도로명주소;
        }

        public String get데이터기준일자() {
            return this.데이터기준일자;
        }

        public void set데이터기준일자(String 데이터기준일자) {
            this.데이터기준일자 = 데이터기준일자;
        }

        public String get관리기관전화번호() {
            return this.관리기관전화번호;
        }

        public void set관리기관전화번호(String 관리기관전화번호) {
            this.관리기관전화번호 = 관리기관전화번호;
        }
    }

    public static class Fields implements Serializable {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
