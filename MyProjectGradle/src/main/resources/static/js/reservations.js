const oldReservations = document.getElementById('pastReservations');


const allReservations= [];

fetch("http://localhost:8080/reservations/api/all")
    .then(response => response.json())
    .then(data=>{
        for (let d of data) {
            allReservations.push(d);
        }
    })

let filterReservation = allReservations.filter( reservation =>{
        let today = new Date();
        let dd= String(today.getDate()).padStart(2, '0');
        let mm = String(today.getMonth() + 1).padStart(2, '0');
        let yyyy = today.getFullYear();
        today = mm + '/' + dd + '/' + yyyy;
        if (reservation.arrivalDate >today) {
            return displayNewReservations(reservation);
        }else {
            displayOldReservations(reservation);
        }
    });

const displayOldReservations = (reservations) => {
    oldReservations.innerHTML = '';
    let row = [];
    let card = document.createElement('div');
    card.className= 'mt-3 rounded badge-info p-3';
    let img = document.createElement('img');
    img.className = 'mr-2';
    img.width="50px";
    img.height = "50px";
    img.src = towns[i].pictureUrl;
    let spanGuest = document.createElement("span");
    spanGuest.className="p-3";
    spanGuest.innerText=GuestName;
    let spanArrivalDate=document.createElement("span");
    spanArrivalDate.className="p-3";
    spanArrivalDate.innerText=arrivalDate;
    let spanDeparture=document.createElement("span");
    spanDeparture.className="p-3";
    spanDeparture.innerText=departure;
    let spanApartment = document.createElement('span');
    spanApartment.className="p-3";
    spanApartment.innerText=apartmentName;
    let aDetails = document.createElement('a');
    aDetails.className="ml-3 p-3 text-warning";
    aDetails.href='/apartments/'+apartment[i].id+'/details';
    aDetails.innerText='ViewApartment';
    let aDetailsReservation = document.createElement('a');
    aDetailsReservation.className="ml-3 p-3 text-warning";
    aDetailsReservation.href='/reservations/'+reservation[i].id+'/details';
    aDetailsReservation.innerText='ReservationDetails';
    let aReservation = document.createElement('a');
    aReservation.className="ml-3 p-3 text-warning";
    aReservation.href='/reservations/'+reservation[i].id+'/delete';
    aReservation.innerText='DeleteReservation';
    card.appendChild(img);
    card.appendChild(spanGuest);
    card.appendChild(spanArrivalDate);
    card.appendChild(spanDeparture);
    card.appendChild(spanApartment);
    card.appendChild(aDetails);
    card.appendChild(aDetailsReservation);
    card.appendChild(aReservation);
    row.push(card);

    for (let i = 0; i <row.length ; i++) {
        oldReservations.appendChild(row[i]);
    }
}
const displayNewReservations = (reservations) => {
    newReservations.innerHTML = '';
    let rowNew = [];
    let card = document.createElement('div');
    card.className= 'mt-3 rounded badge-info p-3';
    let img = document.createElement('img');
    img.className = 'mr-2';
    img.width="50px";
    img.height = "50px";
    img.src = towns[i].pictureUrl;
    let spanGuest = document.createElement("span");
    spanGuest.className="p-3";
    spanGuest.innerText=GuestName;
    let spanArrivalDate=document.createElement("span");
    spanArrivalDate.className="p-3";
    spanArrivalDate.innerText=arrivalDate;
    let spanDeparture=document.createElement("span");
    spanDeparture.className="p-3";
    spanDeparture.innerText=departure;
    let spanApartment = document.createElement('span');
    spanApartment.className="p-3";
    spanApartment.innerText=apartmentName;
    let aDetails = document.createElement('a');
    aDetails.className="ml-3 p-3 text-warning";
    aDetails.href='/apartments/'+apartment[i].id+'/details';
    aDetails.innerText='ViewApartment';
    let aDetailsReservation = document.createElement('a');
    aDetailsReservation.className="ml-3 p-3 text-warning";
    aDetailsReservation.href='/reservations/'+reservation[i].id+'/details';
    aDetailsReservation.innerText='ReservationDetails';
    let aReservation = document.createElement('a');
    aReservation.className="ml-3 p-3 text-warning";
    aReservation.href='/reservations/'+reservation[i].id+'/delete';
    aReservation.innerText='DeleteReservation';
    card.appendChild(img);
    card.appendChild(spanGuest);
    card.appendChild(spanArrivalDate);
    card.appendChild(spanDeparture);
    card.appendChild(spanApartment);
    card.appendChild(aDetails);
    card.appendChild(aDetailsReservation);
    card.appendChild(aReservation);
    rowNew.push(card);

    for (let i = 0; i <rowNew.length ; i++) {
        newReservations.appendChild(rowNew[i]);
    }
}
