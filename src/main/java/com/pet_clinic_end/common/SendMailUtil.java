package com.pet_clinic_end.common;

import com.pet_clinic_end.entity.Exam;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
@Component
public class SendMailUtil  {

    /**
     * 验证邮箱格式
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(email).matches();
    }
    /**
     * 发送验证码(可以根据需求更改相应参数)
     * @param sender    发送人的邮箱
     * @param pwd          邮箱授权码
     * @param receiver      收件人
     * @param code          验证码
     * @return
     */
    public String sendEmail(Integer sendType, String sender, String pwd, String receiver, String code, Exam exam){
        //sendType: 0 code
        //sendType: 1 exam
        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.suixingpay.com");
        props.put("mail.smtp.com", 465);
        props.put("mail.smtp.ssl.enable", true);

        props.setProperty("mail.debug", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.user", sender);
        props.put("mail.password", pwd);
        System.out.println(sender);
        System.out.println(pwd);

        props.put("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.transport.protocol", "smtp");       //使用smpt的邮件传输协议
        props.setProperty("mail.smtp.host", "smtp.163.net");        //主机地址
        props.setProperty("mail.smtp.auth", "true");        //授权通过
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));      //设置发件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver,"用户","utf-8"));     //设置收件人
            message.setSentDate(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 这是我们的邮件内容，可根据需求更改
            String str = "";
            if (sendType==0) {
                str = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body><p style='font-size: 20px;font-weight:bold;'>尊敬的用户 "+receiver+"：</p>"
                        + "<p style='text-indent:2em; font-size: 20px;'>您本次的验证码是 "
                        + "<span style='font-size:30px;font-weight:bold;color:red'>" + code + "</span>，十分钟内有效，请尽快注册！如非本人操作，请忽略！</p>"
                        + "<p style='text-align:right; padding-right: 20px;'"
                        + "<a href='http://120.79.29.170' style='font-size: 18px'>虚拟宠物医院学习系统</a></p>"
                        + "<span style='font-size: 18px; float:right; margin-right: 60px;'>" + sdf.format(new Date()) + "</span></body></html>";

            } else if(sendType==1) {
                message.setSubject("考试通知");
                str = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body><p style='font-size: 20px;font-weight:bold;'>尊敬的用户：</p>"
                        + "<p style='text-indent:2em; font-size: 20px;'>您有一场开始于 "
                        + "<span style='font-size:30px;font-weight:bold;color:red'>" + exam.getStartTime() + "</span> 的考试，"
                        + "考试名称为" + exam.getName() + "，截止时间为<span style='font-size:30px;font-weight:bold;color:red'>" + exam.getEndTime() + "</span>，"
                        + "请注意时间，按时参加！</p>"
                        + "<p style='text-align:right; padding-right: 20px;'"
                        + "<a href='http://120.79.29.170' style='font-size: 18px'>虚拟宠物医院学习系统</a></p>"
                        + "<span style='font-size: 18px; float:right; margin-right: 60px;'>" + sdf.format(new Date()) + "</span></body></html>";

            } else if(sendType==2) {
                str = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body><p style='font-size: 20px;font-weight:bold;'>尊敬的用户 " + receiver + "：</p>"
                        + "<p style='text-indent:2em; font-size: 20px;'>您的密码已经被管理员重置，新密码为 "
                        + "<span style='font-size:30px;font-weight:bold;color:red'>" + code + "</span>，请尽快登录并修改密码！</p>"
                        + "<p style='text-align:right; padding-right: 20px;'"
                        + "<a href='http://120.79.29.170' style='font-size: 18px'>虚拟宠物医院学习系统</a></p>"
                        + "<span style='font-size: 18px; float:right; margin-right: 60px;'>" + sdf.format(new Date()) + "</span></body></html>";
            }
            Multipart mul=new MimeMultipart();  //新建一个MimeMultipart对象来存放多个BodyPart对象
            BodyPart mdp=new MimeBodyPart();  //新建一个存放信件内容的BodyPart对象
            mdp.setContent(str, "text/html;charset=utf-8");
            mul.addBodyPart(mdp);  //将含有信件内容的BodyPart加入到MimeMultipart对象中
            message.setContent(mul); //把mul作为消息内容
            message.saveChanges();
            //创建一个传输对象
            Transport transport=session.getTransport("smtp");
            //建立与服务器的链接  465端口是 SSL传输
            transport.connect("smtp.163.com", 465, sender, pwd);
            //发送邮件
            transport.sendMessage(message,message.getAllRecipients());
            //关闭邮件传输
            transport.close();
            return "验证码发送成功";
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "验证码发送失败";
        }
    }
    /**生成随机的六位验证码*/
    public StringBuilder CreateCode() {
        String dates = "0123456789";
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int index = r.nextInt(dates.length());
            char c = dates.charAt(index);
            code.append(c);
        }
        return code;
    }
}