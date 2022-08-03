let statisticElement = document.getElementById('apartmentStatistic');
let statisticBtn = document.getElementById('statisticsBtn');

const apartmentStatistic= [];

fetch("http://localhost:8080/apartments/api/statistic/{id}")
    .then(response => response.json())
    .then(data=>{
        for (let d of data){
            apartmentStatistic.push(d);
        }
    });

statisticBtn.addEventListener('click', (e) => {
    let filterApartments =apartmentStatistic.filter(statistic => {
        if (statistic.futureProfit > "0" || statistic.pastProfit > "0") {
            return statistic;
        }
    });
    displayApartments(filterApartments);
});

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
}

