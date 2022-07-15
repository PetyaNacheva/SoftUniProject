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

    displayApartments(apartmentStatistic);

});

const displayApartments = (apartments) => {
    statisticElement.innerHTML = '';
    let row = [];
    for (let i = 0; i < apartments.length; i++) {
        let span = document.createElement('span');
        span.innerText=apartments[i].name;
        let pPastMonth=document.createElement('p');
        pPastMonth.innerText=apartments[i].profitFromPastMonth;
        let pFutureMonth = document.createElement('p');
        pFutureMonth.innerText=apartments[i].profitForFutureMonth;
        span.appendChild(pPastMonth);
        span.appendChild(pFutureMonth);
        row.push(span);

    }
    statisticElement.appendChild(row);
}

