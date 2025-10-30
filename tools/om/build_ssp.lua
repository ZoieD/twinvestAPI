oms_setCommandLineOption("--suppressPath=true")
oms_newModel("WindfarmDemo")
oms_addSystem("WindfarmDemo.root", oms_system_wc)

-- FMU（relative path to out/）
oms_addSubModel("WindfarmDemo.root.wind",  "out/TwinvestWindFMUs_WindIn.fmu")
oms_addSubModel("WindfarmDemo.root.price", "out/TwinvestWindFMUs_PriceIn.fmu")
oms_addSubModel("WindfarmDemo.root.wake",  "out/TwinvestWindFMUs_WakeGain.fmu")
oms_addSubModel("WindfarmDemo.root.t1",    "out/TwinvestWindFMUs_TurbinePoly.fmu")
oms_addSubModel("WindfarmDemo.root.t2",    "out/TwinvestWindFMUs_TurbinePoly.fmu")
oms_addSubModel("WindfarmDemo.root.sum",   "out/TwinvestWindFMUs_Sum2.fmu")

-- Connections（port's defination should align with models.mo）
oms_addConnection("WindfarmDemo.root.wind.y",  "WindfarmDemo.root.t1.v")
oms_addConnection("WindfarmDemo.root.wind.y",  "WindfarmDemo.root.wake.u")
oms_addConnection("WindfarmDemo.root.wake.y",  "WindfarmDemo.root.t2.v")
oms_addConnection("WindfarmDemo.root.t1.P",    "WindfarmDemo.root.sum.u1")
oms_addConnection("WindfarmDemo.root.t2.P",    "WindfarmDemo.root.sum.u2")

-- Export SSP
oms_export("WindfarmDemo", "out/MyWindfarm.ssp")
oms_terminate("WindfarmDemo")
oms_delete("WindfarmDemo")
