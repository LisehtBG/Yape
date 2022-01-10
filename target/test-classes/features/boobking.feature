Feature: Booking client

  Scenario Outline: Search client booking
    Given The client "<firstname>" and lastname "<lastname>" have booking
    When I call  post request GetBookingIds
    Then The client has booking
    Examples:
      | firstname | lastname |
      | Sally     | Brown    |

  Scenario Outline: Search for bookingId
    Given The booking id is "<id>"
    When I call get request GetBooking
    Then I get booking information
    Examples:
      | id |
      | 2  |

  Scenario Outline: Create Booking
    Given The client with date  "<firstname>", "<lastname>","<totalprice>", "<depositpaid>", "<checkin>", "<checkout>". "<additionalneeds>"
    When I call  post request CreateBooking
    Then I get booking information for client
    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Jim       | Brown    | 111      | true        | 2018-01-01 | 2019-01-01 |Breakfast        |


  Scenario Outline: Update Booking
    Given The client with date  "<firstname>", "<lastname>","<totalprice>", "<depositpaid>", "<checkin>", "<checkout>". "<additionalneeds>"
    When I call  put request UpdateBooking
    Then I get booking information for client
    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Jim       | Brown    | 111      | true        | 2018-01-01 | 2019-01-01 |Breakfast        |


    Scenario Outline: PartialUpdateBooking
      Given The client "<firstname>" and lastname "<lastname>" want to update booking id  "<id>"
      When I call  path request PartialUpdateBooking
      Then I get Update booking information for client
      Examples:
        | firstname | lastname | id |
        | James     | Brown    | 2  |

    Scenario Outline: Delete Booking
      Given The booking is "<id>"
      And I set authorization
      When I call delete request DeleteBooking
      Then I get status code 201
      Examples:
        | id |
        | 2  |


