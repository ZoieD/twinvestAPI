from fastapi import FastAPI, UploadFile, File, Form
from fastapi.responses import JSONResponse
import json

app = FastAPI()

@app.get("/health")
def health():
    return {"ok": True}

# stage 1：stub（no FMUs, just return fixed values）
@app.post("/simulate-ecos")
async def simulate_ecos(assetZip: UploadFile = File(...), scenario: str = Form(...)):
    _ = await assetZip.read()     # validate uploading function
    sc = json.loads(scenario)     # validate JSON
    T = len(sc.get("windSpeed", [])) or 24
    time = list(range(T))
    # Dummy data：800 kW
    power_kw = [800.0] * T
    dt_hours = sc.get("dt_hours", 1.0)
    total_energy_mwh = sum(power_kw) * dt_hours / 1000.0
    total_income_eur = 0.0
    return JSONResponse({
        "time": time,
        "power_kw": power_kw,
        "total_energy_mwh": total_energy_mwh,
        "total_income_eur": total_income_eur
    })
