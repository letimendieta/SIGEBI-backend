package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.clases.ProcesoProcedimientos;
import com.sigebi.dao.IMovimientoInsumoDao;
import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.dao.IProcedimientosInsumosDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.entity.Areas;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.MovimientosInsumos;
import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcedimientosInsumos;
import com.sigebi.entity.Stock;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.MovimientosInsumosService;
import com.sigebi.service.ProcedimientosInsumosService;
import com.sigebi.service.ProcedimientosService;
import com.sigebi.service.StockService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class ProcedimientosServiceImpl implements ProcedimientosService{

	@Autowired
	private IProcedimientosDao procedimientosDao;
	@Autowired
	private IProcedimientosInsumosDao procedimientosInsumosDao;
	@Autowired
	private ProcedimientosInsumosService procedimientosInsumosService;
	@Autowired
	private StockService stockService;
	@Autowired
	private IStockDao stockDao;
	@Autowired
	private IMovimientoInsumoDao movimientoInsumoDao;
	@Autowired
	private MovimientosInsumosService movimientosInsumosService;
	@Autowired
	private FuncionariosService funcionariosService;
		
	public ProcedimientosServiceImpl(IProcedimientosDao procedimientosDao) {
        this.procedimientosDao = procedimientosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedimientos> findAll() {
		return (List<Procedimientos>) procedimientosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) procedimientosDao.count();
	}
	
	@Override
	@Transactional
	public Procedimientos guardar(Procedimientos procedimientos) {
		return procedimientosDao.save(procedimientos);
	}

	@Override
	@Transactional(readOnly = true)
	public Procedimientos findById(int id) {
		Procedimientos procedimiento = procedimientosDao.findById(id).orElse(null);
		if(procedimiento.getAreas() == null) {
			procedimiento.setAreas(new Areas());
		}
		if( procedimiento.getMotivoConsulta() == null ) {
			procedimiento.setMotivoConsulta(new MotivosConsulta());
		}
		return procedimiento;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedimientos> obtenerProcedimientoPaciente(int pacienteId) {
		List<Integer> pacienteIdList = new ArrayList<Integer>();
		pacienteIdList.add(pacienteId);
		
		Procedimientos procedimiento = new Procedimientos();
		procedimiento.setEstado(Globales.Estados.PENDIENTE);
		
		List<Procedimientos> procedimientosList = buscarNoPaginable(null, null, procedimiento, null, pacienteIdList);
		
		for( Procedimientos proced : procedimientosList ) {
			if( proced.getAreas() == null ) {
				proced.setAreas(new Areas());
			}
			if( proced.getMotivoConsulta() == null ) {
				proced.setMotivoConsulta(new MotivosConsulta());
			}
		}
		
		return procedimientosList;
	}

	@Transactional
	public Procedimientos guardar(ProcesoProcedimientos procesoProcedimiento) throws Exception {		
		
		if( procesoProcedimiento.getProcedimiento().getFuncionarios() == null 
				|| procesoProcedimiento.getProcedimiento().getFuncionarios().getFuncionarioId() == null ){
			throw new SigebiException.BusinessException("Funcionario es requerido ");
		}
		Funcionarios funcionario = 
				funcionariosService.findById(procesoProcedimiento.getProcedimiento().getFuncionarios().getFuncionarioId());
		
		if( Globales.Estados.INACTIVO.equals(funcionario.getEstado()) ){
			throw new SigebiException.BusinessException("El funcionario se encuentra inactivo");
		}
		
		int cantidadMedicamentos = 0;
		for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
			if( procedimientoInsumo.getInsumosMedicos() != null ) {
				cantidadMedicamentos++;
			}
		}
		procesoProcedimiento.getProcedimiento().setCantidadInsumo(cantidadMedicamentos);		
		Procedimientos procedimientoResult = procedimientosDao.save(procesoProcedimiento.getProcedimiento());
		
		//Guardar el procedimiento insumo, realizar el descuento cuando esta en estado ENTREGADO
		try {
			for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
				procedimientoInsumo.setProcedimientos(procedimientoResult);								
				
				//Realizar el descuento de insumos si fue entregado
				if( Globales.EstadosEntregaInsumos.ENTREGADO.equals(procedimientoInsumo.getEstado()) ) {
					int cantidadUsada = 0;
					if ( procedimientoInsumo.getCantidad() != null ) {
						cantidadUsada = procedimientoInsumo.getCantidad();
					}						
					
					if( cantidadUsada > 0) {
						descontarStock(procedimientoInsumo);
						
						//guardar el movimiento de insumos
						
						MovimientosInsumos movimientoInsumo = new MovimientosInsumos();
						
						movimientoInsumo.setCantidadSalida(cantidadUsada);
						movimientoInsumo.setCodProceso(Globales.Procesos.PROCEDIMIENTO);
						movimientoInsumo.setInsumosMedicos(procedimientoInsumo.getInsumosMedicos());
						movimientoInsumo.setMedicamentos(procedimientoInsumo.getMedicamentos());
						movimientoInsumo.setUsuarioCreacion(procedimientoInsumo.getUsuarioCreacion());
						
						movimientoInsumoDao.save(movimientoInsumo);
					}				
				}else {
					procedimientoInsumo.setEstado(Globales.EstadosEntregaInsumos.NOENTREGADO);
				}
				
				procedimientosInsumosDao.save(procedimientoInsumo);
			}			
		} catch (Exception e) {
			throw new Exception("Error al guardar los insumos utilizados " + e.getMessage());
		}
				
		return procedimientoResult;
	}
	
	@Transactional
	public Procedimientos actualizarProcesoProcedimientos(ProcesoProcedimientos procesoProcedimiento) throws Exception {
				
		int cantidadMedicamentos = 0;
		for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
			if( procedimientoInsumo.getMedicamentos() != null 
					&& Globales.EstadosEntregaInsumos.ENTREGADO.equals(procedimientoInsumo.getEstado()) ){
				cantidadMedicamentos++;
			}
		}
		procesoProcedimiento.getProcedimiento().setCantidadInsumo(cantidadMedicamentos);
		procesoProcedimiento.getProcedimiento().setEstado(Globales.Estados.FINALIZADO);
		Procedimientos procedimiento = guardar(procesoProcedimiento.getProcedimiento());		
		
		ProcedimientosInsumos procedimientoBusqueda = new ProcedimientosInsumos();
		procedimientoBusqueda.setProcedimientos(new Procedimientos());
		
		procedimientoBusqueda.getProcedimientos().setProcedimientoId(procesoProcedimiento.getProcedimiento().getProcedimientoId());
				
		try {
			
			List<MovimientosInsumos> movimientosInsumosList = new ArrayList<MovimientosInsumos>();
			
			//Actualizar el estado de los insumos si se cambio
			for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
														
				//Realizar el descuento de insumos si fue entregado
				if( Globales.EstadosEntregaInsumos.ENTREGADO.equals(procedimientoInsumo.getEstado()) ) {
					
					int cantidadUsada = 0;
					if ( procedimientoInsumo.getCantidad() != null ) {
						cantidadUsada = procedimientoInsumo.getCantidad();
					}						
					
					if( cantidadUsada > 0) {
														
						MovimientosInsumos movimientoInsumo = new MovimientosInsumos();
						
						movimientoInsumo.setCantidadSalida(cantidadUsada);
						movimientoInsumo.setCodProceso(Globales.Procesos.PROCEDIMIENTO);
						if(procedimientoInsumo.getInsumosMedicos() != null) {
							movimientoInsumo.setInsumosMedicos(procedimientoInsumo.getInsumosMedicos());
						}	
						if(procedimientoInsumo.getMedicamentos() != null) {
							movimientoInsumo.setMedicamentos(procedimientoInsumo.getMedicamentos());
						}								
						movimientoInsumo.setUsuarioCreacion(procedimientoInsumo.getUsuarioCreacion());
						
						movimientosInsumosList.add(movimientoInsumo);
					}				
				}
				
				ProcedimientosInsumos procedimientoInsumoDb = procedimientosInsumosService.obtener(procedimientoInsumo.getProcedimientoInsumoId());
				if(procedimientoInsumo.getInsumosMedicos() == null || procedimientoInsumo.getInsumosMedicos().getInsumoMedicoId() == null) {
					procedimientoInsumoDb.setInsumosMedicos(null);
				}	
				if(procedimientoInsumo.getMedicamentos() == null || procedimientoInsumo.getMedicamentos().getMedicamentoId() == null) {
					procedimientoInsumoDb.setMedicamentos(null);
				}
				procedimientoInsumoDb.setUsuarioModificacion(procedimientoInsumo.getUsuarioModificacion());
				procedimientoInsumoDb.setEstado(procedimientoInsumo.getEstado());
				
				procedimientosInsumosService.save(procedimientoInsumoDb);
			
			}			
			
			for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
				int cantidadUsada = 0;
				if ( procedimientoInsumo.getCantidad() != null ) {
					cantidadUsada = procedimientoInsumo.getCantidad();
				}						
				
				if( cantidadUsada > 0) {
					descontarStock(procedimientoInsumo);
				}
			}
			
			//guardar el movimiento de insumos
			
			for(MovimientosInsumos moviInsu : movimientosInsumosList) {
				movimientosInsumosService.guardar(moviInsu);
			}
		} catch (Exception e) {
			throw new Exception("Error al actualizar los insumos utilizados " + e.getMessage());
		}
		
		return procedimiento;
	}
		
	@Override
	@Transactional
	public void delete(int id) {
		procedimientosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedimientos> buscar(Date fromDate, Date toDate, 
										Procedimientos procedimiento, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId, String orderBy, String orderDir, Pageable pageable){
		List<Procedimientos> ProcedimientosList;
		
		Specification<Procedimientos> procedimientoSpec = (Specification<Procedimientos>) (root, cq, cb) -> {
		            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }     
            if( pacientesId != null && !pacientesId.isEmpty() ){
            	p = cb.and(root.get("pacientes").in(pacientesId));
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( procedimiento.getProcedimientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("procedimientoId"), procedimiento.getProcedimientoId()) );
            }
            if ( procedimiento.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), procedimiento.getEstado()) );
            }
            if ( procedimiento.getFecha() != null ) {
                //p = cb.and(p, cb.lessThanOrEqualTo(root.get("fecha"), procedimiento.getFecha()) );
                p = cb.and(p, cb.between(root.get("fecha"), procedimiento.getFecha(), procedimiento.getFecha()));
            }
                        
            String orden = "procedimientoId";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }
            return p;
        };
		
        if(pageable != null) {
        	ProcedimientosList = procedimientosDao.findAll(procedimientoSpec, pageable).getContent();			
		}else {
			ProcedimientosList = procedimientosDao.findAll(procedimientoSpec);
		}
        
		for( Procedimientos proced : ProcedimientosList ) {
			if( proced.getAreas() == null ) {
				proced.setAreas(new Areas());
			}
			if( proced.getMotivoConsulta() == null ) {
				proced.setMotivoConsulta(new MotivosConsulta());
			}
		}
		
        return ProcedimientosList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedimientos> buscarNoPaginable(Date fromDate, Date toDate, 
													Procedimientos procedimiento, 
													List<Integer> funcionariosId,
													List<Integer> pacientesId) {
		
		List<Procedimientos> procedimientosList = procedimientosDao.findAll((Specification<Procedimientos>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }     
            if( pacientesId != null && !pacientesId.isEmpty() ){
            	p = cb.and(root.get("pacientes").in(pacientesId));
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if (procedimiento != null && procedimiento.getProcedimientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("procedimientoId"), procedimiento.getProcedimientoId()) );
            }
            if (procedimiento != null && procedimiento.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), procedimiento.getEstado()) );
            }
            if (procedimiento != null && procedimiento.getFecha() != null ) {
                p = cb.and(p, cb.equal(root.get("fecha"), procedimiento.getFecha()) );
            }
           
            cq.orderBy(cb.desc(root.get("procedimientoId")));
            return p;
		});
        return procedimientosList;
    }
	
	public void descontarStock(ProcedimientosInsumos procedimientoInsumo) throws Exception, SigebiException {
		try {
			Stock stockAdescontar = null;
			Stock stock = null;
			String nombre = "";
			int cantidadActual = 0;
			
			if( procedimientoInsumo.getInsumosMedicos() != null && procedimientoInsumo.getInsumosMedicos().getInsumoMedicoId() != null ) {
				stockAdescontar = stockDao.findByInsumosMedicos(procedimientoInsumo.getInsumosMedicos());
				stock = stockAdescontar;
				if( stock == null ) throw new SigebiException.BusinessException("No se encontró stock del insumo " + procedimientoInsumo.getInsumosMedicos().getNombre());
				nombre = stock.getInsumosMedicos().getNombre();
			}else if( procedimientoInsumo.getMedicamentos() != null && procedimientoInsumo.getMedicamentos().getMedicamentoId() != null ) {
				stockAdescontar = stockDao.findByMedicamentos(procedimientoInsumo.getMedicamentos());
				stock = stockAdescontar;
				if( stock == null ) throw new SigebiException.BusinessException("No se encontró stock del medicamento " + procedimientoInsumo.getMedicamentos().getMedicamento());
				nombre = stock.getMedicamentos().getMedicamento();
			}
			
			cantidadActual = stock != null ? stock.getCantidad() : 0;
			int cantidadUsada = procedimientoInsumo.getCantidad();
			
			if( cantidadActual <= 0) {
				throw new SigebiException.BusinessException(nombre +" no cuenta con stock ");
			}
			
			if( cantidadActual < cantidadUsada) {								
				throw new SigebiException.BusinessException(nombre +" no cuenta con stock suficiente, "
						+ "cantidad stock: " + cantidadActual +", "
						+" cantidad usada: " + cantidadUsada);
			}
			
			stock.setCantidad(cantidadActual - cantidadUsada);
			
			stockService.save(stock);
			
		} catch (Exception e) {
			throw new Exception("Error al descontar stock. " + e.getMessage());
		}
	}
	
}
