Feature: Road user acceptance

  Scenario: Valid road id response
    Given a road with the expected data
    |id| a23|
    |displayName|A23|
    |roadStatus|Good|
    |roadstatusDescription|No Exceptional Delays|
    When I call the api with it
    Then the call is successful with result 'response: The status of the A23 is Good and No Exceptional Delays.'

    Given a road with the expected data
    |id| a1|
    |displayName|A1|
    |roadStatus|Serious|
    |roadstatusDescription|Serious Delays|
    When I call the api with it
    Then the call is successful with result 'response: The status of the A1 is Serious and Serious Delays.'

  Scenario: Invalid road id response
    Given an invalid road with the id 'notARoad'
    When I call the api with it
    Then the call is not successful with result 'response: notARoad is not a valid road.'