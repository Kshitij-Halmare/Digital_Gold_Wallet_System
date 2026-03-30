// ── API BASE URL — change if your Spring Boot runs on different port ──
const API_BASE = 'http://localhost:8080';

// ── HELPERS ──
function formatDate(dt) {
  if (!dt) return '—';
  const d = new Date(dt);
  return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

function formatQty(q) {
  if (q == null) return '0.00';
  return parseFloat(q).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function padId(id) {
  return String(id).padStart(3, '0');
}

function extractId(url) {
  if (!url) return '—';
  return url.split('/').filter(Boolean).pop();
}

// ────────────────────────────────────────────────
// STRATEGY 1: Try projection (fastest — 1 request)
// Requires this interface in your Spring Boot project:
//
// @Projection(name = "vendorBranchView", types = VendorBranches.class)
// public interface VendorBranchView {
//     Integer getBranchId();
//     BigDecimal getQuantity();
//     LocalDateTime getCreatedAt();
//     Vendors getVendors();
//     Addresses getAddress();
// }
// ────────────────────────────────────────────────
async function fetchWithProjection() {
  const res = await fetch(
    `${API_BASE}/vendorBranches?projection=vendorBranchView&size=100&sort=branchId,asc`
  );
  if (!res.ok) throw new Error(`projection failed: ${res.status}`);
  const data = await res.json();
  const raw = data._embedded?.vendorBranches || [];

  return raw.map(b => {
    const branchId = b.branchId ?? extractId(b._links?.self?.href);
    const v = b.vendors   || {};
    const a = b.address   || {};
    return {
      branchId,
      quantity:   b.quantity,
      createdAt:  b.createdAt,
      vendorName: v.vendorName ?? '—',
      city:       a.city       ?? '—',
      state:      a.state      ?? '—',
      country:    a.country    ?? '—',
      street:     a.street     ?? '—',
      postalCode: a.postalCode ?? '—',
    };
  });
}

// ────────────────────────────────────────────────
// STRATEGY 2: Fetch branches, then vendor+address
// per branch using _links (works without projection)
// ────────────────────────────────────────────────
async function fetchWithLinks() {
  const res = await fetch(`${API_BASE}/vendorBranches?size=100&sort=branchId,asc`);
  if (!res.ok) throw new Error(`branches fetch failed: ${res.status}`);
  const data = await res.json();
  const branches = data._embedded?.vendorBranches || [];

  if (!branches.length) return [];

  const rows = await Promise.all(branches.map(async b => {
    const branchId = extractId(b._links?.self?.href);

    let vendorName = '—', city = '—', state = '—',
        country = '—', street = '—', postalCode = '—';

    // fetch vendor
    try {
      const vUrl = b._links?.vendors?.href;
      if (vUrl) {
        const vRes = await fetch(vUrl);
        if (vRes.ok) {
          const v = await vRes.json();
          vendorName = v.vendorName ?? '—';
        }
      }
    } catch (_) {}

    // fetch address
    try {
      const aUrl = b._links?.address?.href;
      if (aUrl) {
        const aRes = await fetch(aUrl);
        if (aRes.ok) {
          const a = await aRes.json();
          city       = a.city       ?? '—';
          state      = a.state      ?? '—';
          country    = a.country    ?? '—';
          street     = a.street     ?? '—';
          postalCode = a.postalCode ?? '—';
        }
      }
    } catch (_) {}

    return {
      branchId,
      quantity:   b.quantity,
      createdAt:  b.createdAt,
      vendorName, city, state, country, street, postalCode
    };
  }));

  return rows;
}

// ── MAIN FETCH — tries projection first, falls back to links ──
async function loadData() {
  try {
    const rows = await fetchWithProjection();
    if (rows.length) return rows;
  } catch (_) {}

  // fallback
  return await fetchWithLinks();
}

// ── CACHED ROWS ──
let allRows = [];

// ── POPULATE FILTER DROPDOWNS ──
function populateFilters() {
  const unique = key => [...new Set(allRows.map(r => r[key]).filter(v => v && v !== '—'))].sort();
  fillSelect('cityFilter',    unique('city'));
  fillSelect('stateFilter',   unique('state'));
  fillSelect('countryFilter', unique('country'));
}

function fillSelect(id, values) {
  const sel = document.getElementById(id);
  while (sel.options.length > 1) sel.remove(1);
  values.forEach(v => {
    const opt = document.createElement('option');
    opt.value = v;
    opt.textContent = v;
    sel.appendChild(opt);
  });
}

// ── UPDATE SUMMARY ──
function updateSummary(filtered) {
  const vendorName = allRows[0]?.vendorName ?? '—';
  const totalQty   = filtered.reduce((s, r) => s + parseFloat(r.quantity || 0), 0);

  document.getElementById('summaryVendorName').textContent  = vendorName;
  document.getElementById('summaryBranchCount').textContent = filtered.length;
  document.getElementById('summaryTotalQty').textContent    = formatQty(totalQty) + ' g';
  document.getElementById('pageSubtitle').textContent       = `Manage locations for ${vendorName}`;
}

// ── RENDER TABLE ──
function renderTable(rows) {
  const tbody = document.getElementById('branchTableBody');
  document.getElementById('countBadge').textContent =
    `${rows.length} branch${rows.length !== 1 ? 'es' : ''}`;

  if (!rows.length) {
    tbody.innerHTML = `<tr><td colspan="7" class="table-empty">No branches match the selected filters.</td></tr>`;
    return;
  }

  tbody.innerHTML = rows.map((r, i) => `
    <tr style="animation-delay:${i * 0.03}s">
      <td class="td-id"      data-label="Branch ID">#${padId(r.branchId)}</td>
      <td class="td-city"    data-label="City">${r.city}</td>
      <td class="td-state"   data-label="State">${r.state}</td>
      <td                    data-label="Country">${r.country}</td>
      <td class="td-address" data-label="Address" title="${r.street}, ${r.city} — ${r.postalCode}">
        ${r.street}, ${r.city} — ${r.postalCode}
      </td>
      <td class="td-qty"     data-label="Gold Qty">${formatQty(r.quantity)}</td>
      <td class="td-date"    data-label="Created At">${formatDate(r.createdAt)}</td>
    </tr>
  `).join('');
}

// ── APPLY FILTERS ──
function applyFilters() {
  const city    = document.getElementById('cityFilter').value;
  const state   = document.getElementById('stateFilter').value;
  const country = document.getElementById('countryFilter').value;

  const filtered = allRows.filter(r =>
    (!city    || r.city    === city)    &&
    (!state   || r.state   === state)   &&
    (!country || r.country === country)
  );

  renderTable(filtered);
  updateSummary(filtered);
}

// ── RESET FILTERS ──
function resetFilters() {
  ['cityFilter', 'stateFilter', 'countryFilter'].forEach(id => {
    document.getElementById(id).value = '';
  });
  applyFilters();
}

// ── LOADING / ERROR STATES ──
function showLoading() {
  document.getElementById('branchTableBody').innerHTML = `
    <tr>
      <td colspan="7" class="table-loading">
        <div class="loader"></div>Loading branches from database…
      </td>
    </tr>`;
}

function showError(msg) {
  document.getElementById('branchTableBody').innerHTML = `
    <tr><td colspan="7" class="table-empty">⚠ ${msg}</td></tr>`;
  ['summaryVendorName','summaryBranchCount','summaryTotalQty'].forEach(id => {
    document.getElementById(id).textContent = '—';
  });
  document.getElementById('countBadge').textContent = '0 branches';
}

// ── INIT ──
document.addEventListener('DOMContentLoaded', async () => {
  showLoading();
  try {
    const rows = await loadData();

    if (!rows.length) {
      showError('No branches found in database. Make sure MySQL has data and Spring Boot is running.');
      return;
    }

    allRows = rows;
    populateFilters();
    applyFilters();

  } catch (err) {
    console.error('Failed to load branches:', err);
    showError('Cannot connect to server on port 8080. Start your Spring Boot application first.');
  }
});