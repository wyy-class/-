package com.servlet;

import com.model.UserInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("getMessage".equals(action)){
            try {
                getMessage(req,resp);
            } catch (DocumentException | ParseException e) {
                e.printStackTrace();
            }
        }
        else if("sendMessage".equals(action)){
            sendMessage(req,resp);
        }
        else if("login".equals(action)){
            login(req,resp);
        }
        else if("exit".equals(action)){
            exit(req ,resp);
        }
    }



    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        String username = req.getParameter("username");
        boolean flag=true;
        List<String> lists = UserInfo.getLists();
        if(lists.size()>0&&lists.contains(username)){
            String msg="<script>\n" +
                    "        alert(\"用户名已存在！\");\n" +
                    "    </script>";
            flag=false;
            resp.getWriter().write(msg);
        }

        if(flag){
                lists.add(username);
                HttpSession session = req.getSession();
                session.setAttribute("username",username);
                session.setAttribute("loginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                String newTime=new SimpleDateFormat("yyyyMMdd").format(new Date());
                String url=this.getServletContext().getRealPath("xml/"+newTime+".xml");
                creatFile(url);
                try {
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(new File(url));
                    Element root = document.getRootElement();
                    Element messages = root.element("messages");
                    Element message = messages.addElement("message");
                    message.addElement("from").setText("[系统公告]");
                    message.addElement("face").setText("");
                    message.addElement("to").setText("");
                    message.addElement("content").addCDATA("<font color=\"gray\">"+username+"走进了聊天室"+"</font>");
                    message.addElement("sendTime").setText((String) session.getAttribute("loginTime"));
                    message.addElement("isPrivate").setText("false");
                    OutputFormat format = new OutputFormat();
                    format.setEncoding("GBK");
                    XMLWriter xmlWriter = new XMLWriter(new FileWriter(url),format);
                    xmlWriter.write(document);
                    xmlWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                resp.sendRedirect(req.getContextPath()+"/main.jsp");
            }
    }

    private void creatFile(String url) throws IOException {
        File file = new File(url);
        if(!file.exists()){
            file.createNewFile();
            String dataStr="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            dataStr+="<chat>\n" +
                    "    <messages></messages>\n" +
                    "</chat>";
            byte[] bytes = dataStr.getBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(url);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    private void sendMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html;charset=utf-8");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String face = req.getParameter("face");
        String color = req.getParameter("color");
        String content = req.getParameter("content");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String newDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileUrl = this.getServletContext().getRealPath("xml/" + newDate + ".xml");
        creatFile(fileUrl);
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(fileUrl));
            Element root = document.getRootElement();
            Element sessages = root.element("messages");
            Element message = sessages.addElement("message");
            message.addElement("from").setText(from);
            message.addElement("face").setText(face);
            message.addElement("to").setText(to);
            message.addElement("content").addCDATA("<span style=\"color: "+color+"\">"+content+"</span>");
            message.addElement("sendTime").setText(time);
            OutputFormat format=new OutputFormat();
            format.setEncoding("GBK");
            XMLWriter xmlWriter=new XMLWriter(new FileWriter(fileUrl),format);
            xmlWriter.write(document);
            xmlWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        resp.sendRedirect("/com/message?action=getMessage");
    }

    private void getMessage(HttpServletRequest req, HttpServletResponse resp) throws DocumentException, ParseException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String newDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileUrl = this.getServletContext().getRealPath("xml/" + newDate + ".xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(fileUrl));
        Element root = document.getRootElement();
        Element messages = root.element("messages");
        Iterator iterator = messages.elementIterator("message");

        String msgs="";
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        DateFormat df = DateFormat.getDateTimeInstance();
        while (iterator.hasNext()){
             Element item = (Element) iterator.next();
             String sendTime = item.elementText("sendTime");
//             if(df.parse(sendTime).after(df.parse(session.getAttribute("loginTime").toString()))||
//             df.parse(sendTime).equals(df.parse(session.getAttribute("loginTime").toString()))
//             ){
                 String from = item.elementText("from");
                 String to = item.elementText("to");
                 String face = item.elementText("face");
                 String content = item.elementText("content");
                 String color = item.elementText("color");
                 String send = item.elementText("sendTime");
                 if(to.equals("")||to==null){
                     msgs+="<span>【"+from+"】" +content+": "+send+"</span><br>";
                 }
                 else{
                     msgs+="<span>【"+from+"】 对【"+to+"】【"+face+"】 :"+content+": "+send+"</span><br>";
                 }
//             }
         }
        resp.getWriter().write(msgs);
    }

    private void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String newDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileUrl = this.getServletContext().getRealPath("xml/" + newDate + ".xml");
        creatFile(fileUrl);
        try{
            SAXReader saxReader=new SAXReader();
            Document document = saxReader.read(new File(fileUrl));
            Element root = document.getRootElement();
            Element messages = root.element("messages");
            Element message = messages.addElement("message");
            message.addElement("from").setText("[系统公告]");
            message.addElement("face").setText("");
            message.addElement("to").setText("");
            message.addElement("content").addCDATA("<font color=\"gray\">"+session.getAttribute("username" +
                    "")+"退出了聊天室</font>");
            message.addElement("sendTime").setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            OutputFormat outputFormat=new OutputFormat();
            outputFormat.setEncoding("GBK");
            XMLWriter xmlWriter = new XMLWriter(new FileWriter(fileUrl),outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            session.invalidate();
            List<String> lists = UserInfo.getLists();
            for(int i=0;i<lists.size();i++){
                lists.remove(i);
            }
        }
        req.getRequestDispatcher("/login.jsp").forward(req,resp);
    }
}
