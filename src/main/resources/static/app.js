const API = '/api/assets';

const tableBody = document.querySelector('#assetTable tbody');
const statusFilter = document.getElementById('statusFilter');
const refreshBtn = document.getElementById('refreshBtn');
const addForm = document.getElementById('addForm');
const message = document.getElementById('message');

// --- Read: GET /api/assets (optionally ?status=...) ---
async function loadAssets() {
    const status = statusFilter.value;
    const url = status ? `${API}?status=${encodeURIComponent(status)}` : API;

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const assets = await response.json();   // JSON → JS objects
        renderTable(assets);
    } catch (err) {
        showMessage(`Failed to load: ${err.message}`, false);
    }
}

function renderTable(assets) {
    tableBody.innerHTML = '';
    for (const a of assets) {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${a.id}</td>
            <td>${escapeHtml(a.name)}</td>
            <td>${escapeHtml(a.type)}</td>
            <td>${escapeHtml(a.status)}</td>
            <td>${escapeHtml(a.serialNumber ?? '')}</td>
            <td>${escapeHtml(a.assignedTo ?? '')}</td>
        `;
        tableBody.appendChild(tr);
    }
}

// --- Create: POST /api/assets ---
async function addAsset(event) {
    event.preventDefault();

    const payload = {
        name: document.getElementById('name').value,
        type: document.getElementById('type').value,
        status: document.getElementById('status').value,
        serialNumber: document.getElementById('serialNumber').value || null,
        assignedTo: document.getElementById('assignedTo').value || null
    };

    try {
        const response = await fetch(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)     // JS object → JSON
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error(`HTTP ${response.status}: ${text}`);
        }

        addForm.reset();
        showMessage('Asset added.', true);
        loadAssets();
    } catch (err) {
        showMessage(`Failed to add: ${err.message}`, false);
    }
}

function showMessage(text, ok) {
    message.textContent = text;
    message.className = 'message ' + (ok ? 'ok' : 'error');
}

function escapeHtml(s) {
    return String(s)
        .replace(/&/g, '&amp;').replace(/</g, '&lt;')
        .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

refreshBtn.addEventListener('click', loadAssets);
statusFilter.addEventListener('change', loadAssets);
addForm.addEventListener('submit', addAsset);

loadAssets();   // initial load