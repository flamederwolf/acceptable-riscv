base_dir=$(abspath ..)
sim_dir=$(abspath .)
generated_dir = $(abspath ./generated-src)
generated_dir_debug = $(abspath ./generated-src-debug)
generated_target = $(abspath ../target)
generated_project = $(abspath ../project)
generated_lib = $(abspath ../lib)
generated_pkgs = $(abspath ../Makefrag.pkgs)
generated_testchipip_target = $(abspath ../testchipip/target)
generated_testchipip_project = $(abspath ../testchipip/project)

SBT_INSTALLED=/usr/share/sbt/bin

PROJECT ?= example
MODEL ?= TestHarness
CONFIG ?= DefaultExampleConfig
CFG_PROJECT ?= $(PROJECT)
TB ?= TestDriver

sim = $(sim_dir)/simulator-$(PROJECT)-$(CONFIG)
sim_debug = $(sim_dir)/simulator-$(PROJECT)-$(CONFIG)-debug

default: $(sim)

debug: $(sim_debug)

clean:
	rm -rf *.o *.a simulator-* $(generated_dir) $(generated_dir_debug) DVEfiles $(output_dir) $(generated_target) $(generated_project) $(generated_lib) $(generated_pkgs) $(generated_testchipip_target) $(generated_testchipip_project)

CXXFLAGS := $(CXXFLAGS) -O1 -std=c++11 -I$(RISCV)/include
LDFLAGS := $(LDFLAGS) -L$(RISCV)/lib -Wl,-rpath,$(RISCV)/lib -L$(sim_dir) -lfesvr -lpthread

include $(base_dir)/Makefrag
include $(sim_dir)/Makefrag-verilator

long_name = $(PROJECT).$(MODEL).$(CONFIG)

sim_vsrcs = \
	$(build_dir)/$(long_name).v \
	$(base_dir)/testchipip/vsrc/SimSerial.v

sim_csrcs = \
	$(base_dir)/testchipip/csrc/SimSerial.cc \
	$(base_dir)/testchipip/csrc/verilator-harness.cc

model_dir = $(build_dir)/$(long_name)
model_dir_debug = $(build_dir)/$(long_name).debug

model_header = $(model_dir)/V$(MODEL).h
model_header_debug = $(model_dir_debug)/V$(MODEL).h

model_mk = $(model_dir)/V$(MODEL).mk
model_mk_debug = $(model_dir_debug)/V$(MODEL).mk

$(model_mk): $(sim_vsrcs) $(INSTALLED_VERILATOR)
	mkdir -p $(build_dir)/$(long_name)
	$(VERILATOR) $(VERILATOR_FLAGS) -Mdir $(build_dir)/$(long_name) \
	-o $(sim) $< $(sim_csrcs) -LDFLAGS "$(LDFLAGS)" \
	-CFLAGS "-I$(build_dir) -include $(model_header)"
	touch $@

$(sim): $(model_mk) $(sim_csrcs)
	$(MAKE) VM_PARALLEL_BUILDS=1 -C $(build_dir)/$(long_name) -f V$(MODEL).mk


$(model_mk_debug): $(sim_vsrcs) $(INSTALLED_VERILATOR)
	mkdir -p $(build_dir)/$(long_name).debug
	$(VERILATOR) $(VERILATOR_FLAGS) -Mdir $(build_dir)/$(long_name).debug --trace \
	-o $(sim_debug) $< $(sim_csrcs) -LDFLAGS "$(LDFLAGS)" \
	-CFLAGS "-I$(build_dir) -include $(model_header_debug)"
	touch $@

$(sim_debug): $(model_mk_debug) $(sim_csrcs)
	$(MAKE) VM_PARALLEL_BUILDS=1 -C $(build_dir)/$(long_name).debug -f V$(MODEL).mk
