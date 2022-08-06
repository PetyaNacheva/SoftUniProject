let statisticElement = document.getElementById('apartmentStatistic');
let statisticBtn = document.getElementById('statisticsBtn');
let apartmentId = document.getElementById('apartmentId');

const apartmentStatistic= [];


statisticBtn.addEventListener('click', toggle);

function toggle(e) {

    statisticElement.innerHTML = '';

    let apartmentIdValue =  parseInt(apartmentId.innerHTML);
fetch("http://localhost:8080/apartments/api/statistic/"+apartmentIdValue)
    .then(response => response.json())
    .then(data=>{

            apartmentStatistic.push(data);

    }).then(data => displayApartments(apartmentStatistic));
}

const displayApartments = (apartments) => {
    let row = [];
    console.log(apartments[0].name);
    console.log(apartments[0].profitFromPastMonth);
    console.log(apartments[0].profitForFutureMonth);
    console.log(apartments[0].comingReservations.length);
    console.log(apartments[0].past30DaysReservations.length)

    if(apartments[0].comingReservations.length==0&&apartments[0].past30DaysReservations.length){
        let p = document.createElement('p');
        p.innerText="There no any data for this apartment";
        console.log(p.innerText)
        row.push(p);
    }

    let h5Tag = document.createElement('h5');
    h5Tag.className="text-white py-2 fs-5";
    h5Tag.innerText=apartments[0].name;
    let divContainer = document.createElement('div');
    divContainer.className="container my-2";
    let pProfitLabel = document.createElement('h5');
    pProfitLabel.innerText="Profit from past 30 days reservation";
    pProfitLabel.className="text-white";
    let pProfit =  document.createElement('p');
    if(apartments[0].profitFromPastMonth==0){
        pProfit.innerText="0";
    }
    pProfit.innerText=apartments[0].profitFromPastMonth;
    pProfit.className="fw-bold text-white py-2 fs-5 text-decoration-underline";
    let pProfitFuture =  document.createElement('p');
    pProfitFuture.innerText = apartments[0].profitForFutureMonth;
    pProfitFuture.className="fw-bold text-white py-2 fs-5 text-decoration-underline";
    let pProfitLabelFuture = document.createElement('h5');
    pProfitLabelFuture.innerText="Expected profit from reservations in next 30 days";
    pProfitLabelFuture.className="text-white";
   // console.log(pProfitFuture);
    let tableElement = document.createElement('table');
    let h4=document.createElement('h4');
    let fReservation = document.createElement('p');

   if(apartments[0].comingReservations.length>0){
       h4.className="text-white text-decoration-underline";
       h4.innerText="Future reservations are:"+apartments[0].comingReservations.length;
       tableElement.className="table text-white table-bordered border-white";
       let thead=document.createElement('thead');
       let tr= document.createElement('tr');
       let th1=document.createElement('th');
       th1.scope="col";
       th1.innerText="№"
       let th2=document.createElement('th');
       th2.scope="col";
       th2.innerText="Guest Name"
       let th3=document.createElement('th');
       th3.scope="col";
       th3.innerText="Arrival date"
       let th4=document.createElement('th');
       th4.scope="col";
       th4.innerText="Departure date"
       let th5=document.createElement('th');
       th5.scope="col";
       th5.innerText="Price";
       tr.appendChild(th1);
       tr.appendChild(th2);
       tr.appendChild(th3);
       tr.appendChild(th4);
       tr.appendChild(th5);
       thead.appendChild(tr);
       tableElement.appendChild(thead);
       let tbody=document.createElement('tbody');

       for (let i = 0; i < apartments[0].comingReservations.length; i++) {

           let bodytr = document.createElement('tr');
           let bodyTh = document.createElement('th');
           bodyTh.innerText=i+1;
           let tdGuestName =document.createElement('td');
           tdGuestName.innerText=apartments[0].comingReservations[i].guestName;
           let tdArrival = document.createElement('td');
           tdArrival.innerText = apartments[0].comingReservations[i].arrivalDate;
           let tdDeparture = document.createElement('td');
           tdDeparture.innerText = apartments[0].comingReservations[i].departureDate;
           let tdPrice = document.createElement('td');
           tdPrice.innerText=apartments[0].comingReservations[i].price;
           bodytr.appendChild(bodyTh);
           bodytr.appendChild(tdGuestName);
           bodytr.appendChild(tdArrival);
           bodytr.appendChild(tdDeparture);
           bodytr.appendChild(tdPrice);
           tbody.appendChild(bodytr);
       }
       tableElement.appendChild(tbody);
    }else{
        fReservation.innerText="There has not future reservations for this apartment"
        fReservation.className="text-white";
    }

    let tableElementPast = document.createElement('table');
    let h4Past=document.createElement('h4');
    let pReservations = document.createElement('p');
    if(apartments[0].past30DaysReservations.length>0){
        h4Past.className="text-white text-decoration-underline";
        h4Past.innerText="Future reservations are:"+apartments[0].past30DaysReservations.length;
        tableElementPast.className="table text-white table-bordered border-white";
        let theadPast=document.createElement('thead');
        let trPast= document.createElement('tr');
        let th1Past=document.createElement('th');
        th1Past.scope="col";
        th1Past.innerText="№"
        let th2Past=document.createElement('th');
        th2Past.scope="col";
        th2Past.innerText="Guest Name"
        let th3Past=document.createElement('th');
        th3Past.scope="col";
        th3Past.innerText="Arrival date"
        let th4Past=document.createElement('th');
        th4Past.scope="col";
        th4Past.innerText="Departure date"
        let th5Past=document.createElement('th');
        th5Past.scope="col";
        th5Past.innerText="Price";
        trPast.appendChild(th1Past);
        trPast.appendChild(th2Past);
        trPast.appendChild(th3Past);
        trPast.appendChild(th4Past);
        trPast.appendChild(th5Past);
        theadPast.appendChild(trPast);
        tableElementPast.appendChild(theadPast);
        let tbodyPast=document.createElement('tbody');

        for (let i = 0; i < apartments[0].past30DaysReservations.length; i++) {

            let bodytrPast = document.createElement('tr');
            let bodyThPast = document.createElement('th');
            bodyThPast.innerText=i+1;
            let tdGuestNamePast =document.createElement('td');
            tdGuestNamePast.innerText=apartments[0].past30DaysReservations[i].guestName;
            let tdArrivalPast = document.createElement('td');
            tdArrivalPast.innerText = apartments[0].past30DaysReservations[i].arrivalDate;
            let tdDeparturePast = document.createElement('td');
            tdDeparturePast.innerText = apartments[0].past30DaysReservations[i].departureDate;
            let tdPricePast = document.createElement('td');
            tdPricePast.innerText=apartments[0].past30DaysReservations[i].price;
            bodytrPast.appendChild(bodyThPast);
            bodytrPast.appendChild(tdGuestNamePast);
            bodytrPast.appendChild(tdArrivalPast);
            bodytrPast.appendChild(tdDeparturePast);
            bodytrPast.appendChild(tdPricePast);
            tbodyPast.appendChild(bodytrPast);
        }
        tableElementPast.appendChild(tbodyPast);
    }else {
        pReservations.innerText = "There has no past reservations for this apartment"
        pReservations.className="text-white";
    }


    row.push(h5Tag);
    divContainer.appendChild(pProfitLabel)
    divContainer.appendChild(pProfit);
    divContainer.appendChild(pProfitLabelFuture);
    divContainer.appendChild(pProfitFuture);
    divContainer.appendChild(h4);
    divContainer.appendChild(fReservation)
    divContainer.appendChild(tableElement);
    divContainer.appendChild(h4Past);
    divContainer.appendChild(pReservations);
    divContainer.appendChild(tableElementPast);



    statisticElement.className="container my-2";
    statisticElement.appendChild(h5Tag);
    statisticElement.appendChild(divContainer)
};


   /* for (let i = 0; i < apartments.pastReservations.length; i++) {
        let span = document.createElement('span');
        span.innerText=apartments[i].name;
        let pPastMonth=document.createElement('p');
        pPastMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pPastMonth);

        row.push(span);
    }
    for (let i = 0; i < apartments.futureReservations.length; i++) {
        let span2 = document.createElement('span');
        span2.innerText=apartments[i].name;
        let pFutureMonth=document.createElement('p');
        pFutureMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pFutureMonth);
        row.push(span2);
    }
    */




    /*function toggle(e) {

        statisticElement.innerHTML = '';

        let apartmentId = parseInt(document.getElementById('apartmentId').value);
        fetch("http://localhost:8080/apartments/api/statistic/{id}")
            .then(response => response.json())
            .then(data=>{
                for (let d of data){
                    apartmentStatistic.push(d);
                }
            }).then(data => displayTowns(apartmentStatistic))
      /*  fetch("http://localhost:8080/apartments/api/statistic/" + apartmentId)
            .then(response => response.json())
            .then(data => {
            }).then(data => displayApartments(statisticElement, createApartmentElement, ...data))*/
    /*}

const displayApartments = (apartments) => {
    statisticElement.innerHTML = '';
    let row = [];
    for (let i = 0; i < apartments.pastReservations.length; i++) {
        let span = document.createElement('span');
        span.innerText=apartments[i].name;
        let pPastMonth=document.createElement('p');
        pPastMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pPastMonth);

        row.push(span);
    }
    for (let i = 0; i < apartments.futureReservations.length; i++) {
        let span2 = document.createElement('span');
        span2.innerText=apartments[i].name;
        let pFutureMonth=document.createElement('p');
        pFutureMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pFutureMonth);
        row.push(span2);
    }

    statisticElement.appendChild(row);
}*/

   /* fetch("http://localhost:8080/apartments/api/statistic/{id}")
        .then(response => response.json())
        .then(data=>{

         fetch("http://localhost:8080/reviews/api/" + bookId)
        .then(response => response.json())
        .then(reviews => {
                if (reviews.length === 0) {
                    bookReviewsContainer.innerHTML =
                        `<div class="mb-3">
                            <h5 class="text-left text-secondary">Be the first one to write a review</h5>
                            <h6 class="text-left text-secondary">There are no reviews yet</h6>
                         </div>`;
                    return;
                }

                displayReviews(bookReviewsContainer, createBookReviewElement, ...reviews);
            }
        );
        function displayReviews(container, func, ...reviews) {
    for (const review of reviews) {
        container.appendChild(func(review));
    }
}
            for (let d of data){
                apartmentStatistic.push(d);
            }
        });*/
  /*  let filterApartments =apartmentStatistic.filter(statistic => {
        if (statistic.futureProfit > "0" || statistic.pastProfit > "0") {
            return statistic;
        }
    });
    displayApartments(filterApartments);
});

const displayApartments = (apartments) => {
    apartments.preventDefault();
    statisticElement.innerHTML = '';
    let row = [];
    for (let i = 0; i < apartments.pastReservations.length; i++) {
        let span = document.createElement('span');
        span.innerText=apartments[i].name;
        let pPastMonth=document.createElement('p');
        pPastMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pPastMonth);

        row.push(span);
    }
    for (let i = 0; i < apartments.futureReservations.length; i++) {
        let span2 = document.createElement('span');
        span2.innerText=apartments[i].name;
        let pFutureMonth=document.createElement('p');
        pFutureMonth.innerText=apartments[i].profitFromPastMonth;

        span.appendChild(pFutureMonth);
        row.push(span2);
    }

    statisticElement.appendChild(row);
}
*/
