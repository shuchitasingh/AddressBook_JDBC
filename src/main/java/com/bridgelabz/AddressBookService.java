package com.bridgelabz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;


public class AddressBookService {
    List<Contact> addressBookList;
    AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();

    public List<Contact> readData() {
        addressBookList = addressBookDBService.readData();
        return addressBookList;
    }

    public boolean updateContact(String firstName, String lastName, String phone, String email) {
        int rows = addressBookDBService.updateContact(firstName,lastName,phone,email);
        if(rows>0)
            return true;
        return false;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String firstName,String lastName) {
        List<Contact> checkList = addressBookDBService.getContactDetailsDB(firstName, lastName);
        return checkList.get(0).equals(getContactDetails(firstName, lastName));

    }

    private Contact getContactDetails(String firstName,String lastName) {
        Contact contactData = this.addressBookList.stream()
                .filter(employee->employee.getFirstName().equals(firstName)&&employee.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
        return contactData;
    }

    public List<Contact> getContactInDateRange(String start, String end) {
        return addressBookDBService.getContactInDateRange(start,end);
    }

    public Map<String,Integer> getCountByCityState(AddressBookDBService.CountType type) {
        return addressBookDBService.getCountByCityState(type);
    }

    public void addContact(String firstName, String lastName, String address, String city, String state,
                           String zip, String phone, String email,LocalDate date,String name,String type) {
        addressBookList.add(addressBookDBService.addContact(firstName,lastName,address,city,state,zip,phone,email,date,name,type));
    }

    /**
     * Using MultiThreading to add contact
     * @param asList
     */
    public void addContacts(List<Contact> addContactList) {
        Map<Integer,Boolean> additionStatus = new HashMap<Integer, Boolean>();
        addContactList.forEach(contact -> {
            Runnable task = () -> {
                additionStatus.put(contact.hashCode(), false);
                System.out.println("Contact being added:(threads) "+Thread.currentThread().getName());
                this.addContact(contact.getFirstName(),contact.getLastName(),contact.getAddress(),contact.getCity(),contact.getState(),
                        contact.getZip(),contact.getPhoneNo(),contact.getEmail(),contact.getDate(),contact.getName(),contact.getType());
                additionStatus.put(contact.hashCode(), true);
                System.out.println("Contact added: (threads)"+Thread.currentThread().getName());
            };
            Thread thread = new Thread(task,contact.getFirstName());
            thread.start();
        });
        while(additionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(addressBookList);
    }

}
