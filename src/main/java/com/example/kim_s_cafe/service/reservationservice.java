package com.example.kim_s_cafe.service;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;


import com.example.kim_s_cafe.model.history.historyvo;
import com.example.kim_s_cafe.model.reservation.reservationdao;
import com.example.kim_s_cafe.model.reservation.reservationvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class reservationservice {

    private final boolean yes=true;
    private final boolean no=false;
    private final byte opentime=6;
    private final byte endtime=24;

    @Autowired
    private reservationdao reservationdao;
    @Autowired
    private historyservice historyservice;
    @Autowired
    private timestampservice timestampservice;

    
    public List<Boolean> confirmdate(List<reservationvo>array) {

        if(array.size()>0){
            List<Boolean>checkdate=new ArrayList<>();
            for(int i=0;i<array.size();i++){
                timestampservice.setdates(array.get(i).getReservationdatetime());
                boolean yorn=timestampservice.checktoday();
                if(yorn){
                    checkdate.add(true);
                }else{
                    checkdate.add(false);
                }
            }
            return checkdate;
        }
        return null;
    }
    @Transactional
    public boolean reservationupdate(reservationvo reservationvo) {
        try {
            reservationvo.setSeat(reservationvo.getSeat());
            reservationvo.setRequesthour(reservationvo.getRequesthour());
            Timestamp timestamp=gettimestamp(reservationvo.getRequesthour());
            reservationvo.setReservationdatetime(timestamp);
            reservationvo.setCreated(reservationvo.getCreated());
            reservationdao.save(reservationvo);
            historyservice.updatehistory(reservationvo);
            return yes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }

    public boolean deletereservation(int rid) {
        try {
                reservationdao.deleteById(rid);
                historyservice.deletehistory(rid);
                return yes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }

    public List<reservationvo> findreservation(String email) {
        try {
            List<reservationvo>array=reservationdao.findbyemail(email);
            return array;  
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
    }

    public void log(reservationvo reservationvo,List<Integer> requesthour) {
            for(int i=0;i<requesthour.size();i++){
            System.out.println("????????? ??????????????????"+requesthour.get(i));  
            }
            System.out.println("????????? ?????? ?????? ??????"+reservationvo.getSeat());
            System.out.println("????????? ????????????????????? "+reservationvo.getRemail());
            System.out.println("????????? ?????????????????? "+reservationvo.getRname());
    }  
    public List<Integer> reservationconfirm(String seat) {

        List<Integer>array2=new ArrayList<>();
	    int hour = gethour();
        try {

            List<Integer>array=reservationdao.findbyseat(seat);
        
            if(!array.isEmpty()){///???????????? ???????????????
                for(int ii=opentime;ii<=endtime;ii++){
                    for(int i=0;i<array.size();i++){
                            if(ii==array.get(i)||ii<=hour){
                                System.out.println(ii+"?????? ??????"); 
                                break;   
                            }
                            else{  
                                if(i==array.size()-1){
                                    System.out.println(ii+"??????????????????"); 
                                    array2.add(ii);
                                }
                            }
                    }
                } 
            }
            else{
                for(int i=opentime;i<=endtime;i++){
                    if(i>hour){
                        System.out.println(i+"??????????????????"); 
                        array2.add(i);  
                    }
                }
            }
            return array2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertreservation(reservationvo reservationvo,List<Integer> requesthour) {
        
            try {  
                for(int i=0;i<requesthour.size();i++){
                    reservationvo reservationvo2=new reservationvo();///20210528??????????????? ????????? ???????????????.. ???????????? update??? ?????????..
                    reservationvo2.setRequesthour(requesthour.get(i));
                    Timestamp timestamp=gettimestamp(requesthour.get(i));
                    reservationvo2.setReservationdatetime(timestamp);
                    reservationvo2.setCreated(reservationvo.getCreated());
                    reservationvo2.setRemail(reservationvo.getRemail());
                    reservationvo2.setRname(reservationvo.getRname());
                    reservationvo2.setSeat(reservationvo.getSeat());
                    reservationdao.save(reservationvo2);  
                    historyvo historyvo= historyservice.inserthistory(reservationvo2);
                    historyservice.inserthistory(historyvo);
                }               
                return yes;
            } catch (Exception e) {
                e.printStackTrace();      
            }
            return no;  
    }
    public void check24() {
     
        if(gethour()==0){
            reservationdao.deleteAll();
            System.out.println("24????????????  ?????? ????????? ???????????????");
        }
     
        
    }
    public int gethour() {
        Calendar cal = Calendar.getInstance();
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour+"????????????");
        return hour;  
    }
    private Timestamp gettimestamp(int requesthour){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = sdf.format(date);
        String reservationdatetime=today+" "+requesthour+":0:0";

        return Timestamp.valueOf(reservationdatetime);

    }
 
    
}
