package com.bridgelabz;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AddressBookTest {
    AddressBookService addressBookService;

    @Before
    public void init() {
        addressBookService = new AddressBookService();
    }

    @Test
    public void givenAddressBookData_WhenRetreived_ShouldRetrieveAllContacts() {
        List<Contact> contactList = addressBookService.readData();
        System.out.println(contactList);
        assertEquals(5, contactList.size());
    }

    @Test
    public void givenContactDetails_WhenUpdated_ShouldSyncWithDB() {
        addressBookService.readData();
        boolean isUpdated = addressBookService.updateContact("Bill", "Smith", "123456789", "BillSmith@email.com");
        addressBookService.readData();
        boolean result = addressBookService.checkEmployeePayrollInSyncWithDB("Bill", "Smith");
        assertTrue(result && isUpdated);
    }

    @Test
    public void givenDateRange_ShouldRetrieveContactsInThatRange() {
        addressBookService.readData();
        List<Contact> contactList = addressBookService.getContactInDateRange("2019-01-19", "2020-01-19");
        assertEquals(2, contactList.size());
    }

    @Test
    public void givenStateOrCity_ShouldRetrieveCountOfContactsInThatCityOrState() {
        addressBookService.readData();
        Map<String, Integer> cityMap = addressBookService.getCountByCityState(AddressBookDBService.CountType.CITY);
        Map<String, Integer> stateMap = addressBookService.getCountByCityState(AddressBookDBService.CountType.STATE);
        System.out.println(cityMap);
        System.out.println(stateMap);
        int cityCount = cityMap.get("City 2");
        int stateCount = stateMap.get("California");
        boolean isValid = cityCount == 3 && stateCount == 4;
        assertTrue(isValid);
    }

    @Test
    public void givenAContact_WhenAdded_ShouldSyncWithDatabase() {
        addressBookService.readData();
        addressBookService.addContact("Shuchita", "Singh", "Street 190", "Kanpur", "UP", "456789", "9190919191",
                "suchi@email.com",LocalDate.now(),"name","Family");
        boolean result = addressBookService.checkEmployeePayrollInSyncWithDB("Shuchita", "Singh");
        assertTrue(result);
    }

    @Test
    public void given3Contacts_WhenAddedToDatabase_ShouldMatchContactEntries() {
        addressBookService.readData();
        Contact[] contacts = {
                new Contact("Mark", "Zuckerberg", "Street 200", "NY", "New York", "456781", "9292929292",
                        "mark@email.com",LocalDate.now(),"name","Friend"),
                new Contact("Bill", "Gates", "Street 250", "Medina", "Washington", "666781", "8892929291",
                        "mark@email.com",LocalDate.now(),"name","Friend"),
                new Contact("Jeff", "Bezos", "Street 200", "City 8", "Washington", "456781", "7292929292",
                        "jeff@email.com",LocalDate.now(),"name","Family")
        };
        addressBookService.addContacts(Arrays.asList(contacts));
        List<Contact> newList = addressBookService.readData();
        assertEquals(9, newList.size());
    }
}
