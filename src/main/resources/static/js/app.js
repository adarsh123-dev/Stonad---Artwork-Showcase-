const API = "";
const $ = (s, root=document) => root.querySelector(s);
const $$ = (s, root=document) => [...root.querySelectorAll(s)];
let artworks = [], artists = [], categories = [], authMode = "login";
let currentVisitor = JSON.parse(localStorage.getItem("stonadVisitor") || "null");
let toastTimer;

const esc = value => String(value ?? "").replace(/[&<>"']/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
const imageUrl = path => {
  if (!path) return "";
  if (/^https?:\/\//i.test(path)) return path;
  return "/" + String(path).replace(/^\/+/, "");
};
function notify(message) {
  const el=$("#toast"); el.textContent=message; el.classList.add("show");
  clearTimeout(toastTimer); toastTimer=setTimeout(()=>el.classList.remove("show"),3000);
}
async function request(url, options={}) {
  const response=await fetch(API+url, options);
  const text=await response.text();
  let data=null; try { data=text ? JSON.parse(text) : null; } catch { data=text; }
  if(!response.ok) {
    const message = data && typeof data==="object" ? (data.error || Object.values(data).join(" · ")) : (data || response.statusText);
    throw new Error(message || `Request failed (${response.status})`);
  }
  return data;
}
function showView(name) {
  $$(".view").forEach(v=>v.classList.remove("active"));
  const target=$("#"+name+"View"); if(target) target.classList.add("active");
  $$(".nav-link").forEach(a=>a.classList.toggle("active",a.dataset.view===name));
  $("#mainNav").classList.remove("open");
  window.scrollTo({top:0,behavior:"smooth"});
  if(name==="gallery") renderGallery();
  if(name==="artists") renderArtists();
  if(name==="upload") loadUploadOptions();
  if(name==="profile") loadProfile();
}
$$("[data-view]").forEach(el=>el.addEventListener("click",e=>{e.preventDefault();showView(el.dataset.view)}));
$("#mobileMenu").addEventListener("click",()=>$("#mainNav").classList.toggle("open"));
$("#year").textContent=new Date().getFullYear();

async function loadData() {
  try {
    const [a, ar, c]=await Promise.all([
      request("/api/artworks"), request("/api/artists"), request("/api/categories")
    ]);
    artworks=Array.isArray(a)?a:[]; artists=Array.isArray(ar)?ar:[]; categories=Array.isArray(c)?c:[];
    renderFeatured(); renderGallery(); renderArtists(); populateCategories();
  } catch(err) {
    $("#featuredArt").innerHTML=`<div class="empty-state">Could not load the gallery. Check that the Spring Boot backend and MySQL are running. <br><small>${esc(err.message)}</small></div>`;
    $("#allArt").innerHTML=`<div class="empty-state">Backend connection failed: ${esc(err.message)}</div>`;
    $("#artistGrid").innerHTML=`<div class="empty-state">Artist directory is unavailable right now.</div>`;
  }
}
function artistName(art) { return art?.artist?.name || "Independent artist"; }
function categoryName(art) { return art?.category?.name || "Uncategorised"; }
function artCard(art) {
  const img=imageUrl(art.imageUrl);
  return `<article class="art-card" data-art-id="${art.id}">
    <div class="art-image">${img?`<img src="${esc(img)}" alt="${esc(art.title)}" loading="lazy" onerror="this.parentElement.innerHTML='<div class=&quot;art-placeholder&quot;>✳</div>'">`:`<div class="art-placeholder">✳</div>`}</div>
    <div class="art-meta"><h3>${esc(art.title)}</h3><p>${esc(artistName(art))} <span>·</span> ${esc(categoryName(art))}</p></div>
  </article>`;
}
function attachArtCards(root) {
  $$("[data-art-id]",root).forEach(card=>card.addEventListener("click",()=>openArtwork(Number(card.dataset.artId))));
}
function renderFeatured() {
  const list=artworks.slice(0,4);
  $("#featuredArt").innerHTML=list.length?list.map(artCard).join(""):`<div class="empty-state">No artwork has been published yet. Be the first to share yours.</div>`;
  attachArtCards($("#featuredArt"));
}
function renderGallery(list=artworks) {
  $("#allArt").innerHTML=list.length?list.map(artCard).join(""):`<div class="empty-state">No artwork found. Try another search or category.</div>`;
  $("#galleryCount").textContent=`${list.length} ${list.length===1?"artwork":"artworks"} in the collection`;
  attachArtCards($("#allArt"));
}
function populateCategories() {
  const opts=categories.map(c=>`<option value="${c.id}">${esc(c.name)}</option>`).join("");
  $("#categoryFilter").innerHTML=`<option value="">All categories</option>${opts}`;
  $("#uploadCategory").innerHTML=`<option value="">Choose category</option>${opts}`;
}
function renderArtists() {
  $("#artistGrid").innerHTML=artists.length?artists.map(a=>`<article class="artist-card" data-artist-id="${a.id}">
    <div class="artist-avatar">${esc((a.name||"A").charAt(0).toUpperCase())}</div><h3>${esc(a.name)}</h3>
    <p>${esc(a.bio||"An independent creative sharing original work on STONAD.")}</p>
    <small>${esc(a.email||"STONAD ARTIST")}　↗</small></article>`).join(""):`<div class="empty-state">No artists found yet.</div>`;
  $$("[data-artist-id]",$("#artistGrid")).forEach(card=>card.addEventListener("click",()=>openArtist(Number(card.dataset.artistId))));
}
async function openArtwork(id) {
  try {
    const art=await request(`/api/artworks/${id}`);
    const img=imageUrl(art.imageUrl);
    $("#detailContent").innerHTML=`<div class="detail-layout">${img?`<img src="${esc(img)}" alt="${esc(art.title)}">`:`<div class="art-placeholder">✳</div>`}
      <div><p class="eyebrow">${esc(categoryName(art))}</p><h2>${esc(art.title)}</h2><p>By ${esc(artistName(art))}</p><p>${esc(art.description||"No description has been added for this artwork yet.")}</p></div></div>`;
    $("#detailModal").classList.remove("hidden");
  } catch(e){notify(e.message)}
}
async function openArtist(id) {
  const artist=artists.find(a=>a.id===id); if(!artist)return;
  const works=artworks.filter(a=>a.artist?.id===id);
  $("#detailContent").innerHTML=`<div><p class="eyebrow">ARTIST PROFILE</p><h2 style="font:500 34px 'Playfair Display'">${esc(artist.name)}</h2><p style="color:#817987;font-size:12px">${esc(artist.email||"")}</p><p style="color:#817987;font-size:12px;line-height:1.8">${esc(artist.bio||"This artist is part of the STONAD creative community.")}</p><h3 style="font:500 21px 'Playfair Display';margin-top:28px">Artwork by ${esc(artist.name)}</h3><div class="art-grid" style="grid-template-columns:repeat(2,minmax(0,1fr))">${works.length?works.map(artCard).join(""):'<p>No artwork added yet.</p>'}</div></div>`;
  $("#detailModal").classList.remove("hidden"); attachArtCards($("#detailContent"));
}
$("#searchInput").addEventListener("input",filterGallery);
$("#categoryFilter").addEventListener("change",filterGallery);
$("#clearSearch").addEventListener("click",()=>{$("#searchInput").value="";filterGallery()});
function filterGallery() {
  const q=$("#searchInput").value.trim().toLowerCase(), cat=$("#categoryFilter").value;
  renderGallery(artworks.filter(a=>(!q||(a.title||"").toLowerCase().includes(q))&&(!cat||String(a.category?.id)===cat)));
}
$$("[data-close-modal]").forEach(b=>b.addEventListener("click",closeAuth));
function closeAuth(){$("#authModal").classList.add("hidden")}
$$("[data-close-detail]").forEach(b=>b.addEventListener("click",()=>$("#detailModal").classList.add("hidden")));
$("#loginButton").addEventListener("click",()=>openAuth("login"));
$("#joinButton").addEventListener("click",()=>openAuth("register"));
$("#profileButton").addEventListener("click",()=>showView("profile"));
function openAuth(mode) {
  authMode=mode; $("#authModal").classList.remove("hidden"); $("#authMessage").textContent="";
  $("#authTitle").innerHTML=mode==="login"?'Come on <em>in.</em>':'Join the <em>creative side.</em>';
  $("#authNameWrap").classList.toggle("hidden",mode==="login");
  $("#authName").required=mode==="register";
  $("#authSubmit").innerHTML=mode==="login"?'Log in <span>↗</span>':'Create account <span>↗</span>';
  $$(".auth-tab").forEach(t=>t.classList.toggle("active",t.dataset.auth===mode));
}
$$("[data-auth]").forEach(b=>b.addEventListener("click",()=>openAuth(b.dataset.auth)));
$("#authForm").addEventListener("submit",async e=>{
  e.preventDefault(); const fd=new FormData(e.currentTarget);
  const payload={email:fd.get("email"),password:fd.get("password")};
  if(authMode==="register") payload.name=fd.get("name");
  else payload.name="Visitor"; // The current backend validates the Visitor entity's name during login.
  const msg=$("#authMessage"); msg.textContent="Please wait…"; msg.classList.remove("error");
  try {
    const visitor=await request(authMode==="login"?"/api/visitors/login":"/api/visitors/register",{
      method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(payload)
    });
    currentVisitor=visitor; localStorage.setItem("stonadVisitor",JSON.stringify(visitor));
    updateHeader(); closeAuth(); notify(authMode==="login"?"Welcome back!":"Your STONAD account is ready.");
    e.currentTarget.reset();
  } catch(err){msg.textContent=err.message;msg.classList.add("error")}
});
function updateHeader() {
  const logged=!!currentVisitor;
  $("#loginButton").classList.toggle("hidden",logged);
  $("#joinButton").classList.toggle("hidden",logged);
  $("#profileButton").classList.toggle("hidden",!logged);
  if(logged) $("#profileButton").textContent=(currentVisitor.name||"A").charAt(0).toUpperCase();
}
function loadProfile() {
  if(!currentVisitor){openAuth("login");return}
  $("#profileName").value=currentVisitor.name||""; $("#profileEmail").value=currentVisitor.email||"";
  $("#profileInitial").textContent=(currentVisitor.name||"A").charAt(0).toUpperCase();
}
$("#profileForm").addEventListener("submit",async e=>{
  e.preventDefault(); if(!currentVisitor)return;
  const payload={name:$("#profileName").value.trim(),email:$("#profileEmail").value.trim(),password:"ProfileUpdate123"};
  // The current Visitor entity requires a password for @Valid; the service updates only name and email.
  try {
    const updated=await request(`/api/visitors/${currentVisitor.id}`,{method:"PUT",headers:{"Content-Type":"application/json"},body:JSON.stringify(payload)});
    currentVisitor=updated;localStorage.setItem("stonadVisitor",JSON.stringify(updated));updateHeader();
    $("#profileMessage").textContent="Profile updated successfully.";$("#profileMessage").classList.remove("error");notify("Profile updated.");
  } catch(err){$("#profileMessage").textContent=err.message;$("#profileMessage").classList.add("error")}
});
$("#logoutButton").addEventListener("click",()=>{currentVisitor=null;localStorage.removeItem("stonadVisitor");updateHeader();showView("home");notify("You have logged out.")});
$("#deleteProfileButton").addEventListener("click",async()=>{
  if(!currentVisitor||!confirm("Delete your visitor account? This cannot be undone."))return;
  try{await request(`/api/visitors/${currentVisitor.id}`,{method:"DELETE"});currentVisitor=null;localStorage.removeItem("stonadVisitor");updateHeader();showView("home");notify("Visitor account deleted.")}catch(e){notify(e.message)}
});
async function loadUploadOptions() {
  if(!artists.length||!categories.length) await loadData();
  $("#uploadArtist").innerHTML=`<option value="">Choose artist</option>`+artists.map(a=>`<option value="${a.id}">${esc(a.name)}</option>`).join("");
}
$("#artImage").addEventListener("change",e=>{$("#fileName").textContent=e.target.files[0]?.name||"No file selected"});
$("#uploadForm").addEventListener("submit",async e=>{
  e.preventDefault(); const fd=new FormData(e.currentTarget);
  if(!fd.get("image")?.size){notify("Please choose an image.");return}
  if(fd.get("image").size>5*1024*1024){notify("Image must be 5 MB or smaller.");return}
  const msg=$("#uploadMessage");msg.textContent="Uploading…";msg.classList.remove("error");
  try{
    await request("/api/artworks/upload",{method:"POST",body:fd});
    msg.textContent="Artwork uploaded successfully!";notify("Your artwork is now in the gallery.");
    e.currentTarget.reset();$("#fileName").textContent="No file selected";await loadData();
  }catch(err){msg.textContent=err.message;msg.classList.add("error")}
});
updateHeader();loadData();
