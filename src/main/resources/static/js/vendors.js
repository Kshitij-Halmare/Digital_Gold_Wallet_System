// ── API BASE URL ──
const API_BASE = 'http://localhost:8080';

// ── HELPERS ──
function formatDate(dt) {
  if (!dt) return '—';
  const d = new Date(dt);
  return d.toLocaleDateString('en-IN', {
    day: '2-digit', month: 'short', year: 'numeric'
  });
}

function formatQty(q) {
  if (q == null) return '0.00';
  return parseFloat(q).toLocaleString('en-IN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}

function padId(id) {
  return String(id).padStart(3, '0');
}

function extractIdFromUrl(url) {
  if (!url) return '—';
  const parts = url.split('/');
  return parts[parts.length - 1];
}

// remove `{?projection}`
function cleanUrl(url) {
  return url ? url.replace(/\{.*\}/, '') : null;
}

// ── FETCH ALL BRANCHES ──
async function fetchBranches() {
  const res = await fetch(`${API_BASE}/branches?size=100&sort=branchId,asc`);
  if (!res.ok) throw new Error(`Failed to fetch branches: ${res.status}`);

  const data = await res.json();

  // IMPORTANT: your backend uses this key
  return data._embedded?.branches || [];
}

// ── FETCH RELATED DATA ──
async function fetchRelated(branch) {
  const links = branch._links;

  // --- Vendor ---
  let vendor = { vendorName: '—' };

  try {
    const res = await fetch(cleanUrl(links.vendors.href));

    if (res.ok) {
      const v = await res.json();

      vendor = {
        vendorName: v.vendorName || '—'
      };
    }

  } catch (_) {}

  // --- Address (already in projection) ---
  const address = branch.address || {
    street: '—',
    city: '—',
    state: '—',
    postalCode: '—',
    country: '—'
  };

  // --- Counts ---
  const txCount     = await fetchCount(cleanUrl(links.transactions?.href));
  const holdCount   = await fetchCount(cleanUrl(links.holdings?.href));
  const physTxCount = await fetchCount(cleanUrl(links.physicalTransactions?.href));

  return { vendor, address, txCount, holdCount, physTxCount };
}

// ── FETCH COUNT ──
async function fetchCount(url) {
  if (!url) return 0;

  try {
    const res = await fetch(url);
    if (!res.ok) return 0;

    const data = await res.json();
    const embedded = data._embedded;

    if (!embedded) return 0;

    const key = Object.keys(embedded)[0];
    return embedded[key]?.length ?? 0;

  } catch (_) {
    return 0;
  }
}

// ── BUILD CARD ──
function buildCard(item, index) {
  const { branch, vendor, address, txCount, holdCount, physTxCount } = item;
  const branchId = extractIdFromUrl(branch._links?.self?.href);

  return `
    <div class="branch-card" style="animation-delay:${index * 0.07}s">
      <div class="branch-card-bar"></div>

      <div class="branch-card-head">
        <div class="branch-vendor">
          <span>${vendor.vendorName}</span>
          Branch #${branchId}
        </div>
        <div class="branch-head-right">
          <span class="branch-id-badge">ID · ${padId(branchId)}</span>
          <div class="qty-chip">
            <span class="qty-value">${formatQty(branch.quantity)}</span>
            <span class="qty-label">grams (gold)</span>
          </div>
        </div>
      </div>

      <div class="branch-card-body">
        <div class="info-block">
          <div class="info-label">City</div>
          <div class="info-value highlight">${address.city}</div>
        </div>
        <div class="info-block">
          <div class="info-label">State</div>
          <div class="info-value highlight">${address.state}</div>
        </div>
        <div class="info-block">
          <div class="info-label">Postal Code</div>
          <div class="info-value">${address.postalCode}</div>
        </div>
        <div class="info-block">
          <div class="info-label">Country</div>
          <div class="info-value">${address.country}</div>
        </div>
        <div class="info-block full">
          <div class="info-label">Street Address</div>
          <div class="info-value">${address.street}</div>
        </div>
        <div class="info-block">
          <div class="info-label">Created At</div>
          <div class="info-value">${formatDate(branch.createdAt)}</div>
        </div>

        <!-- ✅ FIXED: vendor name instead of vendorId -->
        <div class="info-block">
          <div class="info-label">Vendor</div>
          <div class="info-value">${vendor.vendorName}</div>
        </div>
      </div>

      <div class="branch-card-stats">
        <div class="stat-item">
          <span class="stat-num">${txCount}</span>
          <span class="stat-lbl">Transactions</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">${holdCount}</span>
          <span class="stat-lbl">Holdings</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">${physTxCount}</span>
          <span class="stat-lbl">Physical Txns</span>
        </div>
      </div>
    </div>
  `;
}

// ── CACHE ──
let allBranchData = [];

// ── RENDER ──
function renderBranches() {
  const grid = document.getElementById('branchGrid');
  const badge = document.getElementById('countBadge');

  badge.textContent = `${allBranchData.length} branches`;

  grid.innerHTML = allBranchData.map((item, i) => buildCard(item, i)).join('');
}

// ── LOADING ──
function showLoading() {
  document.getElementById('branchGrid').innerHTML = `
    <div class="loading-state">
      <span>Loading branches…</span>
    </div>`;
}

// ── ERROR ──
function showError(msg) {
  document.getElementById('branchGrid').innerHTML = `
    <div class="empty-state"><span>⚠</span>${msg}</div>`;
}

// ── INIT ──
document.addEventListener('DOMContentLoaded', async function () {
  console.log("UPDATED JS LOADED ✅"); // debug

  showLoading();

  try {
    const branches = await fetchBranches();

    if (branches.length === 0) {
      showError('No branches found in the database.');
      return;
    }

    const relatedList = await Promise.all(branches.map(b => fetchRelated(b)));

    allBranchData = branches.map((branch, i) => ({
      branch,
      ...relatedList[i]
    }));

    renderBranches();

  } catch (err) {
    console.error(err);
    showError('Server error. Check backend.');
  }
});