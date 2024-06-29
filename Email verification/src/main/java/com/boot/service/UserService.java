package com.boot.service;

import com.boot.model.Student;

public interface UserService {
    Student saveStudent(Student student, String path);
    void removeSessionMessage();
    void sendEmail(Student student, String path);
    boolean verifyAccount(String verificationCode);
}
