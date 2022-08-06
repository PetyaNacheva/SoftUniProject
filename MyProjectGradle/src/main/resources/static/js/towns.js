const townsList = document.getElementById('cardsList')

const allTowns= [];


fetch("http://localhost:8080/towns/api/top3")
    .then(response => response.json())
    .then(data=>{
        for (let d of data){
            allTowns.push(d);
        }
    }).then(data => displayTowns(allTowns));

const displayTowns = (towns) => {

    townsList.innerHTML = '';
    let row = [];
    for (let i = 0; i < towns.length; i++) {
        let card = document.createElement('div');
        card.classList.add('text-center');
        let cardDiv=document.createElement('div');
        cardDiv.className='card bg-light mb-3 m-4 border border-0 rounded';
        let divImg=document.createElement('div');
        divImg.className='card-body bg-success p-4 bg-opacity-50 rounded fs-4';
        let img = document.createElement('img');
        img.className = 'card-img';
        img.src = towns[i].pictureUrl;
        img.alt = 'Card image cap';
        let cardBody = document.createElement('div');
        cardBody.className = 'card-body';
        let cardTitle = document.createElement('div');
        cardTitle.className = 'text-white fs-5 py-2';
        cardTitle.innerText = towns[i].name;
        let cardText = document.createElement('p');
        cardText.className = 'card-text my-1 text-truncate text-white py-2';
        cardText.innerText =towns[i].description;
        let block = document.createElement('th:block');
        block.se
        let a = document.createElement('a');
        a.className = 'btn bg-warning text-primary';
        a.href = '/towns/'+towns[i].id+'/details';
        a.innerText = 'Visit';

        divImg.appendChild(cardTitle)
        divImg.appendChild(img);
        divImg.appendChild(cardText)
        divImg.appendChild(a);
        cardDiv.appendChild(divImg);
        card.appendChild(cardDiv);
        row.push(card);
        if ((i + 1) % 3 === 0 || towns.length - i === 1) {

            for (let j = 0; j < row.length; j++) {
                let cardGroup = document.createElement('div');
                cardGroup.className='container card-columns my-4 justify-content-between col-4';
                cardGroup.appendChild(row[j]);
                townsList.appendChild(cardGroup);
            }

            row = [];
        }
    }
}
