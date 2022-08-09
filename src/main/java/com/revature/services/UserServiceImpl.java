package com.revature.services;

import com.revature.models.User;
import com.revature.repositories.UserRepository;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private JavaMailSender mailSender;

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByCredentials(String email, String password) {
    return userRepository.findByEmailAndPassword(email, password);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findById(int id) {
    return userRepository.findById(id);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public void sendEmail(String email, int id) {
    try {
      String subject = "Password Reset Request";
      String siteurl =
        "http://aac53e81081b042258fc80efa94a009c-104931072.us-east-1.elb.amazonaws.com:3000/reset-password/" +
        id;
      String senderName = "RevatureMerchTeam";
      String mailContent =
        "<p>Click the link below to change your password</p>" +
        "<a href=\"http://aac53e81081b042258fc80efa94a009c-104931072.us-east-1.elb.amazonaws.com:3000/reset-password/" +
        id +
        "\"> Link to Reset Password</a>" +
        "<br> <p>Thank you for shopping with us</p>";
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);

      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(mailContent, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
