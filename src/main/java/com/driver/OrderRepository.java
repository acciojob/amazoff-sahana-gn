package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        orderMap.put(order.getOrderID(),order);
        // your code here
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            if(orderMap.containsKey(orderId)&& partnerMap.containsKey(partnerId)){
                orderToPartnerMap.put(orderId,partnerId);
                partnerToOrderMap.computeIfAbsent(partnerId,k->new HashSet<>()).add(orderId);
            }
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.getOrDefault(orderId,null);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.getOrDefault(partnerId,null);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerToOrderMap.getOrDefault(partnerId,new HashSet<>()).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId,new HashSet<>()));
        // your code here
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return  new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerMap.containsKey(partnerId)){
            HashSet<String> orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
            for(String orderID:orders){
                orderToPartnerMap.remove(orderID);
            }
            partnerToOrderMap.remove(partnerId);
            partnerMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if(orderMap.containsKey(orderId)){
            String partnerID=orderToPartnerMap.get(orderId);
            if(partnerID!=null&&partnerToOrderMap.containsKey(partnerID)){
                partnerToOrderMap.get(partnerID).remove(orderId);
            }
            orderToPartnerMap.remove(orderId);
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        return orderMap.size() - orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int count = 0;
        if(partnerToOrderMap.containsKey(partnerId)){
            int givenTime = convertTimeToMinutes(timeString);
            for(String orderId : partnerToOrderMap.get(partnerId)){
                int orderTime = convertTimeToMinutes(orderMap.get(orderId).getDeliveryTime());

                if(orderTime > givenTime){
                    count++;
                }
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int latestTime = 0;
        if(partnerToOrderMap.containsKey(partnerId)){
            for(String orderId : partnerToOrderMap.get(partnerId)){
                int orderTime = Integer.parseInt(orderMap.get(orderId).getDeliveryTime());
                latestTime = Math.max(latestTime, orderTime);
            }
        }
        return latestTime > 0 ? convertMinutesToTime(latestTime) : "00:00";


    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private String convertMinutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }




}