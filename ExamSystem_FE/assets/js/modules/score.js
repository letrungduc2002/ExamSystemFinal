document.addEventListener("DOMContentLoaded", renderScore);

function renderScore() {

    const result = JSON.parse(
        sessionStorage.getItem("exam_result")
    );

    if (!result) {
        alert("Không có dữ liệu kết quả");
        return;
    }

    document.getElementById("correctCount")
        .innerText = `Số câu đúng: ${result.correctAnswers}`;

    const tbody = document.getElementById("scoreTable");

    result.details.forEach((q, index) => {

        const userOption = q.options
            .find(o => o.id === q.selectedOptionId);

        const correctOption = q.options
            .find(o => o.id === q.correctOptionId);

        const optionsHtml = q.options.map((o, i) => {

            const letter = String.fromCharCode(65 + i);

            return `
            <div class="option">
                ${letter}. ${o.content}
            </div>
            `;

        }).join("");

        const row = `
        <tr>

            <td>${index + 1}</td>

            <td>
                <b>${q.content}</b>
                <div class="options">
                    ${optionsHtml}
                </div>
            </td>

            <td class="${q.correct ? "correct" : "wrong"}">
                ${userOption ? userOption.content : "Không chọn"}
            </td>

            <td class="correct">
                ${correctOption ? correctOption.content : ""}
            </td>

        </tr>
        `;

        tbody.insertAdjacentHTML("beforeend", row);

    });

}

function goHome() {

    window.location.href = "/index.html?page=exam";

}