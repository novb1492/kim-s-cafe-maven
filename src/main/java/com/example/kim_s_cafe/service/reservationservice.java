package com.example.kim_s_cafe.service;


import java.util.ArrayList;
import java.util.List;

import com.example.kim_s_cafe.model.reservation.reservationdao;
import com.example.kim_s_cafe.model.reservation.reservationvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class reservationservice {
    private final boolean yes=false;
    private final boolean no=true;

    @Autowired
    private reservationdao reservationdao;
    
    public void log(reservationvo reservationvo,List<Integer> requesthour) {
            for(int i=0;i<requesthour.size();i++)
            {
                System.out.println("예약을 시도하는시간"+requesthour.get(i));  
            }
            System.out.println("예약을 시도 하는 자리"+reservationvo.getSeat());
            System.out.println("예약을 시도하는이메일 "+reservationvo.getRemail());
            System.out.println("예약을 시도하는이름 "+reservationvo.getRname());
    } 
    public ArrayList<Integer> reservationconfirm(String seat) {
        System.out.println("예약검사"+seat);
        ArrayList<Integer>arrayList=new ArrayList<>();
        try {
            List<reservationvo>array=reservationdao.findbyseat(seat);
            for(int i=0;i<array.size();i++)
            {
                reservationvo reservationvo=array.get(i);///아하 이렇게 꺼내는거구나 20210524
                System.out.println(reservationvo.getRequesthour()+"예약시간");
               //arrayList.add(reservationvo.getHour());
               // arrayList.add(reservationvo.getHour()+reservationvo.getRentaltime());
            }
          for(int i=0; i<arrayList.size();i++)
          {
              if(i%2==0)
              {
                System.out.println(arrayList.get(i)+"예약시간");
              }
              else
              {
                System.out.println(arrayList.get(i)+"종료시간");
              }
          }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public boolean insertreservation(reservationvo reservationvo) {
      
            try {
                reservationdao.save(reservationvo);
                //int a=reservationdao.onlyinsert(reservationvo.getRemail(), reservationvo.getRequesthour(), reservationvo.getRname(), reservationvo.getSeat());
                return yes;
            } catch (Exception e) {
                e.printStackTrace();      
            }
            return no;  
    }
 
    
}
