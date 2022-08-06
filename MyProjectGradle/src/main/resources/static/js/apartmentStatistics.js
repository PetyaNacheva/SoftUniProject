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
    let fututeReservations = document.createElement('p');
    fututeReservations.innerText=apartments[0].comingReservations.length;

    console.log(fututeReservations);
    let pastReservations = document.createElement('p');
    pastReservations.innerText=apartments[0].past30DaysReservations.length;
    console.log(fututeReservations);

    row.push(h5Tag);
    divContainer.appendChild(pProfitLabel)
    divContainer.appendChild(pProfit);
    divContainer.appendChild(pProfitLabelFuture);
    divContainer.appendChild(pProfitFuture);
   // divContainer.appendChild(fututeReservations);
    //divContainer.appendChild(pastReservations);


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
