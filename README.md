# SoftUniProject

 # The idea of the project: 

<span>Small application that could be useed as Apartment Reservation System. There has two main scenarios: the user could be a host of the apartments, or be a guset of any apartment.
There has admin and user, as user if own appartment will be automaticly set as host. If the same user make a reservation for any other apartment which is upload so the user will receive the role of guest. 
The admin would have access to all operations of adding, deleting or modifiying any apartments from the users. 
Admin is the first register user in the app and also will be responsible for adding, deleting or updating of all information for the towns that are populated in the database. Also the admin will have access to statistic panel with information for the wuantity of the apartments or reservations.<span>

<h2>Funsctionality<h2>
  <ul>
<li> public part (accessible without authentication) - the non-authenticated users can see:
  <ol type="1">
  <li> Home page - welcome page that show top 3 of the towns with the most published apartments.</li>
  <li> Login page - contains a login form.</li>
  <li> Register page - contains a registration form.</li>
  <li> Info page - it contains full information about the company, contact details.</li>
  <li> Error page - a funny "page not found 404" page pops up when someone tries to reach a non-existing page.</li>
  </ol>
</li>
<li> private part (available for logined/registered users) - it contains the following pages:
   <ol type="1">
  <li> Home page - on the home page are also top 3 of the towns with most published apartments, but there also a visit button that will redirect to the information page for the town.</li>
  <li> Home page - search section - at the bottom of the home page is shown the search section which will allow user to find the appropriate apatment in the desire town.</li>
  <li>Town page - the user could find list of all towns and with view btn to subscribe all datailed information for the town as picture.</li>
  <li>Make reservation tag- redirect the user to the search section on the home page.</li>
  <li>My reservation tag - on this section the user could explore all own reservations:</li>
      <li> Arrival today - on this section will be shown the reservation that is with arival day on the same day</li>
      <li> New reservations - all of the reservations that are with arrival day in the future</li>
      <li> Past reservations - on this section will be shown all reservations for past one year.</li>
      <li> For each one of the reservations the user will be able to see details for the apartment or the reservation details.</li>
      <li> From details reservation page the user is able to delete the reservation if the arrival date is later than 1 day from the current date.</li>
      <li> Be a Host page - from this page the user could register own apartment as all fields in the form are mandatory.</li> 
      <li> My Apartments page - from this page the user could view all of owned apartments.</li>
      <li> Apartment details page - if the user is onwer could update infomration for the aparment, could apply more pictures, but if the picture is only one it could not be deleted. From the button statistic the user could see the reservations for futrue 30 days and for past 30 days and also the profit and expected profit. If the current apartment do not have available reservations it could be deleted.</li>
      <li> Userprofile page - from userpforile page the user is able to change personal information except username. The user is able to change the profile picture or the system will apply by default empty one.
     </ol>
</li>
<li> administrator part (available for admins only):
  <ol type="1">
   <li> Admin page - it is visible only for users with admin roles. This page contains the statistic result for the reservations, all apartments and all visitations made on the app.</li>
     <li> Add town page - it can be accessed from the admin and will allow to add or delete a towns</li>
    <li> Edit town page - it can be accessed from the town details page by clicking on a special button. It holds an option for editing.</li>
    <li> The admin own all functionality of the user also and is bale to delete or update any apartment or reservation.</li>
    </ol>
  </li>
</ul>
<hr>

<h3>Project structure:</h3>
<p>The project structure is a standard MVC</p>
For this application are user CSS, HTML, JQuery, Thymeleaf and JavaScript for the Front-End and Java (Spring framework, Hibernate) for the Back-End part along with MySQL.
About the testing of the application are used JUnit5 - implemented in Unit and Integration tests.

To start the application clone the "final" repository from github, put your credentials for the MySQL DB and make an enviroment variable named "TWILIO" - the value of this variable will be given on the project defence, owing some security reasons.
To enter as an admin please use:
username: admin
password: admin
